package world.gregs.hestia.core.network.codec.inbound.packet

import io.netty.channel.ChannelHandler
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.packets.InboundPacket
import world.gregs.hestia.core.services.load.PacketMap

@ChannelHandler.Sharable
class PacketHandler<T : InboundPacket>(private val packets: PacketMap<T>) : PacketInboundHandler<T>() {
    override val logger = LoggerFactory.getLogger(PacketHandler::class.java)!!

    override fun getHandler(opcode: Int): T? {
        return packets.getPacket(opcode)
    }

}