package world.gregs.hestia.network.codec.inbound

import world.gregs.hestia.network.Session
import world.gregs.hestia.network.packets.InboundPacket
import world.gregs.hestia.network.packets.Packet
import world.gregs.hestia.network.packets.PacketMap
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor
import org.slf4j.LoggerFactory

@ChannelHandler.Sharable
open class PacketInboundHandler(private val packets: PacketMap) : SessionInboundHandler() {

    private var channels: ChannelGroup = DefaultChannelGroup("channels", GlobalEventExecutor.INSTANCE)
    open val open: Boolean = true

    override fun handle(session: Session, buffer: ByteBuf) {
        val packet = Packet(buffer.array())
        while (packet.readableBytes() > 0 && session.channel?.isOpen == true && open) {
            val packetId = packet.readUnsignedByte()
            val handler = packets.getPacket(packetId)
            val packetSize = packets.getSize(packetId)

            if(handler == null || packetSize == null) {
                if(packetId != 16)
                logger.warn("Unhandled packet: $packetId ${packet.readableBytes()}")
                break
            }

            packet.opcode = packetId

            //Process packet size
            var length = when(packetSize) {
                -1 -> packet.readUnsignedByte()
                -2 -> packet.readShort()
                -3 -> packet.readInt()
                -4 -> {
                    val readable = packet.readableBytes()
                    logger.warn("Packet ${packet.opcode} is missing a proper size. Might be $readable")
                    readable
                }
                else -> packetSize
            }

            if (length > packet.readableBytes()) {
                logger.warn("Packet ${packet.opcode} $packetSize has too large size $length ${packet.readableBytes()}")
                length = packet.readableBytes()
            }

            session.ping = System.currentTimeMillis()

            //Process the packet
            process(session, handler, packet, length)

            packet.buffer.skipBytes(packet.buffer.readableBytes())
        }
    }

    open fun process(session: Session, handler: InboundPacket, packet: Packet, length: Int) {
        handler.read(session, packet, length)
    }

    override fun connect(session: Session) {
        channels.add(session.channel)
    }

    override fun disconnect(session: Session) {
        channels.remove(session.channel)
    }

    fun stop() {
        channels.close().awaitUninterruptibly()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PacketInboundHandler::class.java)
    }
}