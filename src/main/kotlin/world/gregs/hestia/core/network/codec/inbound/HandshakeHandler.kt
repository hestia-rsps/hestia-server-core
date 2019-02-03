package world.gregs.hestia.core.network.codec.inbound

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.AttributeKey
import org.slf4j.Logger
import world.gregs.hestia.core.network.Session
import world.gregs.hestia.core.network.getSession
import world.gregs.hestia.core.network.packets.Packet
import java.io.IOException

@ChannelHandler.Sharable
abstract class HandshakeHandler : ChannelInboundHandlerAdapter() {

    abstract val logger: Logger
    abstract val key: AttributeKey<Boolean>

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        if(ctx == null || msg == null) {
            return
        }

        val session = ctx.getSession()
        if(msg is Packet) {
            val channel = ctx.channel()
            if (!channel.hasAttr(key)) {
                handshake(session, msg)
                channel.attr(key).set(true)
            }
        } else if(msg is ByteBuf) {
            process(session, msg)
        } else {
            logger.warn("Unhandled message: $msg")
        }
    }

    abstract fun handshake(session: Session, buffer: Packet)

    abstract fun process(session: Session, buffer: ByteBuf)

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if(cause is IOException) {
            logger.debug(cause.message)
        } else {
            cause.printStackTrace()
        }
        ctx.close()
    }
}