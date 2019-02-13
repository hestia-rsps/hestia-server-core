package world.gregs.hestia.core.network.codec.message

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.codec.Codec
import world.gregs.hestia.core.network.codec.packet.Packet
import java.io.IOException

/**
 * Decodes [Packet]s into [Message]s using decoders found in the supplied codec
 * @param codec The codec containing decoders to process
 */
@ChannelHandler.Sharable
open class SimpleMessageDecoder(private val codec: Codec) : MessageToMessageDecoder<Packet>() {

    internal val logger = LoggerFactory.getLogger(this::class.java)!!

    @Suppress("UNCHECKED_CAST")
    override fun decode(ctx: ChannelHandlerContext, msg: Packet, out: MutableList<Any>) {
        val decoder = codec.get(msg.opcode) as? MessageDecoder<Message>
                ?: run {
            logger.warn("No decoder for packet: ${msg.opcode}")
            return
        }

        try {
            decoder.write(ctx, msg, out)
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