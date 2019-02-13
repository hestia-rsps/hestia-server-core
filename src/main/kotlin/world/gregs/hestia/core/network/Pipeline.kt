package world.gregs.hestia.core.network

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel
import world.gregs.hestia.core.network.NetworkConstants.SESSION_KEY

/**
 * Pipeline
 * Easy to use [ChannelInitializer] with support for [Session], shared handlers and handlers instanced every initiation
 */
@ChannelHandler.Sharable
class Pipeline(private val action: (ChannelPipeline) -> Unit) : ChannelInitializer<SocketChannel>() {
    private var sharedHandlers = ArrayList<Pair<String?, ChannelHandler>>()

    fun add(sharedHandler: ChannelHandler, name: String? = null) {
        sharedHandlers.add(Pair(name, sharedHandler))
    }

    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) {
        ch.attr(SESSION_KEY).setIfAbsent(Session(ch))

        action.invoke(ch.pipeline())

        sharedHandlers.forEach { if(it.first == null) ch.pipeline().addLast(it.second) else ch.pipeline().addLast(it.first, it.second) }
    }

}