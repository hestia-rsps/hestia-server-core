package world.gregs.hestia.core.network.codec.inbound

import world.gregs.hestia.core.network.NetworkConstants
import world.gregs.hestia.core.network.Session
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.slf4j.LoggerFactory
import java.io.IOException

@ChannelHandler.Sharable
abstract class InboundHandler : ChannelInboundHandlerAdapter() {

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is ByteBuf) {
            return
        }

        //Mark beginning
        msg.markReaderIndex()

        try {
            //Check size
            val available = msg.readableBytes()
            if (available < 1 || available > NetworkConstants.INBOUND_DATA_LIMIT) {
                logger.warn("Packet size breaches data limitations.")
                return
            }

            //Copy from unsafe buffer
            val buffer = Unpooled.buffer(available)
            msg.readBytes(buffer)

            //Handle data
            handle(ctx, buffer)
        } catch (er: Throwable) {
            er.printStackTrace()
        }
    }

    /**
     * Handles the [InboundPacket]
     */
    abstract fun handle(ctx: ChannelHandlerContext, buffer: ByteBuf)

    /**
     * Checks buffer for basic opcode & size
     */
    fun validate(session: Session, buffer: ByteBuf, opcode: Int, size: Int = -1): Boolean {
        val op = buffer.readUnsignedByte().toInt()
        if (op != opcode) {
            logger.info("Invalid opcode: $op expected: $opcode")
            return false
        }

        if (size != -1 && buffer.readableBytes() != size) {
            logger.info("Invalid size ${buffer.readableBytes()}")
            session.close()
            return false
        }

        return true
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if(cause is IOException) {
            logger.debug(cause.message)
        } else {
            cause.printStackTrace()
        }
        ctx.close()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(InboundHandler::class.java)
    }
}