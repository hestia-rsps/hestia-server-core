package world.gregs.hestia.core.network.codec.inbound

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.NetworkConstants
import world.gregs.hestia.core.network.Session

abstract class SessionInboundHandler : InboundHandler() {

    override fun handle(ctx: ChannelHandlerContext, buffer: ByteBuf) {
        val session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get()
                ?: throw IllegalStateException("Session is null")
        handle(session, buffer)
    }

    abstract fun handle(session: Session, buffer: ByteBuf)

    override fun channelActive(ctx: ChannelHandlerContext) {
        val session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get()
                ?: throw IllegalStateException("Session is null")
        connect(session)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        val session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get()
                ?: throw IllegalStateException("Session is null")
        disconnect(session)
    }

    open fun connect(session: Session) {
    }

    open fun disconnect(session: Session) {
    }

}