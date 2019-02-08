package world.gregs.hestia.core.network.codec.decode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.slf4j.Logger
import world.gregs.hestia.core.network.NetworkConstants
import java.io.IOException

/**
 * Decoder
 * Processes incoming bytes
 */
abstract class Decoder : ByteToMessageDecoder() {

    abstract val logger: Logger

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
            if (`in`.readableBytes() > NetworkConstants.INBOUND_DATA_LIMIT) {
                logger.warn("Packet size breaches data limitations.")
                return
            }

            process(ctx, `in`, out)
        } catch (t: Throwable) {
            logger.debug("Error Decoding: ", t)
        }
    }

    /**
     * By default all data is added to [out] for processing
     */
    open fun process(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        out.add(buf.readBytes(buf.readableBytes()))
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (cause is IOException) {
            logger.debug(cause.message)
        } else {
            cause.printStackTrace()
        }
        ctx.close()
    }
}