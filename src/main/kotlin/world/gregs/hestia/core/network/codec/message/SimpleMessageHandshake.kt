package world.gregs.hestia.core.network.codec.message

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.HandshakeDispatcher

/**
 * Basic implementation of [MessageHandshake] using a [HandshakeDispatcher]
 */
@ChannelHandler.Sharable
open class SimpleMessageHandshake(private val dispatcher: HandshakeDispatcher) : MessageHandshake() {

    override fun process(ctx: ChannelHandlerContext, message: Message, handshake: Boolean) {
        dispatcher.dispatch(ctx, message, handshake)
    }
}