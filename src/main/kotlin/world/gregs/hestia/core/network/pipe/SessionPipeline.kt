package world.gregs.hestia.core.network.pipe

import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel
import world.gregs.hestia.core.network.NetworkConstants
import world.gregs.hestia.core.network.Session

/**
 * Pipeline
 * Easy to use [Pipeline] with support for [Session]
 */
class SessionPipeline(action: (ChannelPipeline) -> Unit) : Pipeline(action) {

    @Throws(Exception::class)
    override fun initChannel(ch: SocketChannel) {
        ch.attr(NetworkConstants.SESSION_KEY).setIfAbsent(Session(ch))
        super.initChannel(ch)
    }

}