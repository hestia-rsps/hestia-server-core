package world.gregs.hestia.core.network.protocol.decoders

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.message.MessageDecoder
import world.gregs.hestia.core.network.protocol.ClientOpcodes.LOGIN_HANDSHAKE
import world.gregs.hestia.core.network.protocol.decoders.messages.LoginHandshake
import world.gregs.hestia.core.network.packet.Packet

class LoginHandshakeDecoder : MessageDecoder<LoginHandshake>(0, LOGIN_HANDSHAKE) {

    override fun decode(ctx: ChannelHandlerContext, packet: Packet): LoginHandshake? {
        return LoginHandshake()
    }

}