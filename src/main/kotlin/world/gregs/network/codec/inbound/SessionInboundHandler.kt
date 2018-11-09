package world.gregs.network.codec.inbound

import world.gregs.network.NetworkConstants
import world.gregs.network.Session
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

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