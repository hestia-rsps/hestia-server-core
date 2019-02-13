package world.gregs.hestia.core.network.codec.message

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.util.AttributeKey
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * Processes [Message]s with a handshake attribute
 * Relies on [MessageDecoder]
 */
@ChannelHandler.Sharable
abstract class MessageHandshake : SimpleChannelInboundHandler<Message>() {

    val logger = LoggerFactory.getLogger(this::class.java)!!
    internal val key = AttributeKey.valueOf<Boolean>(this::class.java.simpleName)!!

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        process(ctx, msg, shook(ctx))
    }

    /**
     * Processes all messages
     * @param ctx Channel
     * @param message The message to process
     * @param handshake Whether the handshake is complete
     */
    abstract fun process(ctx: ChannelHandlerContext, message: Message, handshake: Boolean)

    /**
     * Completes the handshake
     * @param ctx The channel
     */
    open fun shake(ctx: ChannelHandlerContext) {
        ctx.channel().attr(key).set(true)
    }

    fun shook(ctx: ChannelHandlerContext): Boolean {
        return ctx.channel().attr(key).get() ?: false
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