package world.gregs.hestia.core.network.codec.debug

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.Dispatcher
import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.codec.message.SimpleMessageHandler

@ChannelHandler.Sharable
open class DebugMessageHandler(dispatcher: Dispatcher) : SimpleMessageHandler(dispatcher) {

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        logger.debug("Read $msg")
        super.channelRead0(ctx, msg)
    }

}