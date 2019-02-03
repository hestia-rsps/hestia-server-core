package world.gregs.hestia.core.network.codec.inbound.packet

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import world.gregs.hestia.core.network.Session
import world.gregs.hestia.core.network.getSession
import world.gregs.hestia.core.network.packets.InboundPacket
import world.gregs.hestia.core.network.packets.Packet
import java.io.IOException

@ChannelHandler.Sharable
abstract class PacketInboundHandler<T> : ChannelInboundHandlerAdapter(), PacketProcessor<T> {

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        if(msg !is Packet || ctx == null) {
            return
        }
        val session = ctx.getSession()
        process(session, msg)
    }

    override fun process(session: Session, handler: T, packet: Packet, length: Int) {
        if (handler is InboundPacket) {
            handler.read(session, packet, length)
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if(cause is IOException) {
            logger.debug(cause.message)
        } else {
            cause.printStackTrace()
        }
        ctx.close()
    }
}