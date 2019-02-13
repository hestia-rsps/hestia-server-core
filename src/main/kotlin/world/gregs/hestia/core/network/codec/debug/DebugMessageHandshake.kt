package world.gregs.hestia.core.network.codec.debug

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.HandshakeDispatcher
import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.codec.message.SimpleMessageHandshake

@ChannelHandler.Sharable
open class DebugMessageHandshake(dispatcher: HandshakeDispatcher) : SimpleMessageHandshake(dispatcher) {

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        logger.debug("Read $msg handshake: ${ctx.channel().hasAttr(key)}")
        super.channelRead0(ctx, msg)
    }

    override fun process(ctx: ChannelHandlerContext, message: Message, handshake: Boolean) {
        logger.debug("Process $message $handshake")
        super.process(ctx, message, handshake)
    }

    override fun shake(ctx: ChannelHandlerContext) {
        logger.debug("Handshake complete ${key.name()}.")
        super.shake(ctx)
    }
}