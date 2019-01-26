package world.gregs.hestia.core.network.codec.inbound

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.Session
import world.gregs.hestia.core.network.packets.InboundPacket
import world.gregs.hestia.core.network.packets.Packet
import world.gregs.hestia.core.services.load.PacketMap

@ChannelHandler.Sharable
open class PacketInboundHandler<T>(override val packets: PacketMap<T>) : SessionInboundHandler(), PacketProcessor<T> {

    private var channels: ChannelGroup = DefaultChannelGroup("channels", GlobalEventExecutor.INSTANCE)
    override val logger = LoggerFactory.getLogger(PacketInboundHandler::class.java)!!
    override val open: Boolean = true

    override fun handle(session: Session, buffer: ByteBuf) {
        super.process(session, Packet(buffer.array()))
    }

    override fun process(session: Session, handler: T, packet: Packet, length: Int) {
        if (handler is InboundPacket) {
            handler.read(session, packet, length)
        }
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
}