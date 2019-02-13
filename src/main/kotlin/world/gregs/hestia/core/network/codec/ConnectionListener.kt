package world.gregs.hestia.core.network.codec

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import world.gregs.hestia.core.network.Session
import world.gregs.hestia.core.network.getSession

/**
 * Handles activated and deactivated sessions
 */
abstract class ConnectionListener : ChannelInboundHandlerAdapter() {

    override fun channelActive(ctx: ChannelHandlerContext) {
        connect(ctx.getSession())
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        disconnect(ctx.getSession())
    }

    abstract fun connect(session: Session)

    abstract fun disconnect(session: Session)

}