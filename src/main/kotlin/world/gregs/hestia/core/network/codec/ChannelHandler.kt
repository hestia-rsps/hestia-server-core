package world.gregs.hestia.core.network.codec

import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor
import world.gregs.hestia.core.network.Session

/**
 * Handles a list of all connections
 * Used for closing all simultaneously (shutdown)
 */
class ChannelHandler : ConnectionListener() {

    private var channels: ChannelGroup = DefaultChannelGroup("channels", GlobalEventExecutor.INSTANCE)

    override fun connect(session: Session) {
        channels.add(session.channel)
    }

    override fun disconnect(session: Session) {
        channels.remove(session.channel)
    }

    fun stop() {
        channels.close().awaitUninterruptibly()
    }
}