package world.gregs.hestia.core.network.codec.message

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.codec.Codec
import java.io.IOException

/**
 * Encodes [Message]s into [Packet]s using encoders found in the supplied codec
 * @param codec The codec containing encoders to process
 */
@ChannelHandler.Sharable
open class SimpleMessageEncoder(private val codec: Codec) : MessageToByteEncoder<Message>() {

    internal val logger = LoggerFactory.getLogger(this::class.java)!!

    @Suppress("UNCHECKED_CAST")
    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf) {
        val encoder = codec.get(msg::class) as? MessageEncoder<Message>
                ?: run {
                    logger.warn("No encoder for message: $msg")
                    return
                }

        try {
            encoder.write(msg, out)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (cause !is IOException) {
            logger.debug("Exception: ", cause)
        }
        ctx.close()
    }

}