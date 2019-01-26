package world.gregs.hestia.core.network.codec.inbound.packet

import io.netty.channel.ChannelHandler
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.Session
import world.gregs.hestia.core.network.packets.InboundPacket
import world.gregs.hestia.core.network.packets.Packet
import world.gregs.hestia.core.services.load.PacketMap

/**
 * PacketHandler
 * A basic implementation of PacketInboundHandler for single [PacketMap] handling
 */
@ChannelHandler.Sharable
class PacketHandler<T : InboundPacket>(private val packets: PacketMap<T>) : PacketInboundHandler<T>() {
    override val logger = LoggerFactory.getLogger(PacketHandler::class.java)!!

    override fun getHandler(opcode: Int): T? {
        return packets.getPacket(opcode)
    }

    override fun process(session: Session, handler: T?, packet: Packet) {
            handler?.read(session, packet)
    }

}