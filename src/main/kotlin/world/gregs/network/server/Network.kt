package world.gregs.network.server

import world.gregs.network.server.threads.DecoderThreadFactory
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.slf4j.LoggerFactory

class Network(private val boss: EventLoopGroup = createGroup(true), private val worker: EventLoopGroup = createGroup(false), channel: ChannelInitializer<SocketChannel>, private val name: String = "Server") : ServerBootstrap() {
    init {
        group(boss, worker)
        channel(NioServerSocketChannel::class.java)
        childHandler(channel)
        option(ChannelOption.SO_REUSEADDR, true)
        option(ChannelOption.SO_BACKLOG, 128)
        childOption(ChannelOption.TCP_NODELAY, true)
        childOption(ChannelOption.SO_KEEPALIVE, true)
    }

    fun start(port: Int): ChannelFuture {
        val future = bind(port).syncUninterruptibly()
        logger.info("$name bound to port $port")
        return future
    }

    fun finish() {
        boss.shutdownGracefully()
        worker.shutdownGracefully()
    }

    companion object {
        fun createGroup(boss: Boolean): NioEventLoopGroup {
            val serverWorkersCount = if(boss) {
                1
            } else {
                val processors = Runtime.getRuntime().availableProcessors()
                if (processors >= 6) processors - if (processors >= 12) 7 else 5 else 1
            }
            return NioEventLoopGroup(serverWorkersCount, DecoderThreadFactory())
        }
        private val logger = LoggerFactory.getLogger(Network::class.java)
    }
}