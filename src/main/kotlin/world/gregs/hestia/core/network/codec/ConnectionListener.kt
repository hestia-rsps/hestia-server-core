package world.gregs.hestia.core.network.codec

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

/**
 * Handles activated and deactivated channels
 */
abstract class ConnectionListener : ChannelInboundHandlerAdapter() {

    override fun channelActive(ctx: ChannelHandlerContext) {
        connect(ctx)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        disconnect(ctx)
    }

    abstract fun connect(ctx: ChannelHandlerContext)

    abstract fun disconnect(ctx: ChannelHandlerContext)

}