package world.gregs.hestia.core.network.codec.decode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * Processes incoming bytes
 */
abstract class ByteDecoder : ByteToMessageDecoder() {

    internal val logger = LoggerFactory.getLogger(this::class.java)!!

    /**
     * Decodes incoming bytes makes sure there is data and it's less than the [NetworkConstants.INBOUND_DATA_LIMIT]
     * Data is passed onto [process]
     */
    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        //Make sure has header
        if (`in`.readableBytes() < 1) {
            return
        }

        //Mark beginning
        `in`.markReaderIndex()

        try {
            //Check size limitations
            if (`in`.readableBytes() > INBOUND_DATA_LIMIT) {
                logger.warn("Packet size breaches data limitations.")
                return
            }

            process(ctx, `in`, out)
        } catch (t: Throwable) {
            logger.debug("Error Decoding: ", t)
        }
    }

    /**
     * Process all data decoded
     */
    abstract fun process(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>)

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (cause !is IOException) {
            logger.debug("Exception: ", cause)
        }
        ctx.close()
    }

    companion object {
        private const val INBOUND_DATA_LIMIT = 7500
    }
}