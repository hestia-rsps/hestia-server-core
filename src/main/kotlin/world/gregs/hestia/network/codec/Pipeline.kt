package world.gregs.hestia.network.codec

import world.gregs.hestia.network.NetworkConstants.SESSION_KEY
import world.gregs.hestia.network.Session
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

@ChannelHandler.Sharable
class Pipeline(private vararg val handlers: ChannelHandler) : ChannelInitializer<SocketChannel>() {

    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) {

        ch.attr(SESSION_KEY).setIfAbsent(Session(ch))

        handlers.forEach { ch.pipeline().addLast(it) }
    }

}