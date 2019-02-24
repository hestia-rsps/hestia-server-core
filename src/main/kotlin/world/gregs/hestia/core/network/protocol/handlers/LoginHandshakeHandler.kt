package world.gregs.hestia.core.network.protocol.handlers

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.client.Response
import world.gregs.hestia.core.network.clientRespond
import world.gregs.hestia.core.network.codec.message.MessageHandler
import world.gregs.hestia.core.network.protocol.decoders.messages.LoginHandshake

class LoginHandshakeHandler : MessageHandler<LoginHandshake> {

    override fun handle(ctx: ChannelHandlerContext, message: LoginHandshake) {
        ctx.clientRespond(Response.DATA_CHANGE)
    }

}