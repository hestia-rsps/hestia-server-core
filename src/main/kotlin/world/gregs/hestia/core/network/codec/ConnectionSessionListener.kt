package world.gregs.hestia.core.network.codec

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.Session
import world.gregs.hestia.core.network.getSession

/**
 * Handles activated and deactivated sessions
 */
abstract class ConnectionSessionListener : ConnectionListener() {

    override fun connect(ctx: ChannelHandlerContext) {
        connect(ctx.getSession())
    }

    override fun disconnect(ctx: ChannelHandlerContext) {
        disconnect(ctx.getSession())
    }

    abstract fun connect(session: Session)

    abstract fun disconnect(session: Session)

}