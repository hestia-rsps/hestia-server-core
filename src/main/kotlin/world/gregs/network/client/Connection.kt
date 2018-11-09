package world.gregs.network.client

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel

class Connection(channel: ChannelInitializer<io.netty.channel.socket.SocketChannel>) : Bootstrap() {
    var future: ChannelFuture? = null
        private set

    init {
        group(NioEventLoopGroup())
        channel(NioSocketChannel::class.java)
        handler(channel)
        option(ChannelOption.SO_REUSEADDR, true)
        option(ChannelOption.TCP_NODELAY, true)
        option(ChannelOption.SO_KEEPALIVE, true)
    }

    fun start(host: String, port: Int): ChannelFuture {
        future = connect(host, port).syncUninterruptibly()
        return future!!
    }

    fun finish() {
        future?.channel()?.close()
        future = null
    }
}