package world.gregs.hestia.core.network.codec.message

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.codec.Dispatcher
import java.io.IOException
/**
 * Dispatches [Message]s from pipeline to [Dispatcher]
 * @param dispatcher The dispatcher to send messages too
 */
@ChannelHandler.Sharable
open class SimpleMessageHandler(private val dispatcher: Dispatcher) : SimpleChannelInboundHandler<Message>() {

    internal val logger = LoggerFactory.getLogger(this::class.java)!!

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        dispatcher.dispatch(ctx, msg)
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (cause !is IOException) {
            logger.debug("Exception: ", cause)
        }
        ctx.close()
    }

}