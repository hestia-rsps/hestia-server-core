package world.gregs.hestia.core.network.protocol.decoders

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.message.MessageDecoder
import world.gregs.hestia.core.network.protocol.ClientOpcodes.PING
import world.gregs.hestia.core.network.protocol.decoders.messages.Ping
import world.gregs.hestia.core.network.packet.Packet

class PingDecoder : MessageDecoder<Ping>(0, PING) {

    override fun decode(ctx: ChannelHandlerContext, packet: Packet): Ping? {
        return Ping()
    }

}