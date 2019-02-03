package world.gregs.hestia.core.network.codec

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import world.gregs.hestia.core.network.NetworkConstants.SESSION_KEY
import world.gregs.hestia.core.network.Session
import kotlin.reflect.KClass

@ChannelHandler.Sharable
class Pipeline : ChannelInitializer<SocketChannel>() {
    private var sharedHandlers = ArrayList<ChannelHandler>()
    private var instanceHandlers = ArrayList<KClass<out ChannelHandler>>()

    fun add(vararg sharedHandlers: ChannelHandler) {
        this.sharedHandlers.addAll(sharedHandlers)
    }

    fun add(vararg instanceHandlers: KClass<out ChannelHandler>) {
        this.instanceHandlers.addAll(instanceHandlers)
    }

    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) {

        ch.attr(SESSION_KEY).setIfAbsent(Session(ch))

        instanceHandlers.forEach { ch.pipeline().addLast(it.java.getDeclaredConstructor().newInstance()) }

        sharedHandlers.forEach { ch.pipeline().addLast(it) }

    }

}