package world.gregs.hestia.core.network.codec

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.codec.message.MessageHandler
import kotlin.reflect.KClass

/**
 * Implementation of [HandshakeDispatcher]
 * Handles two codecs one regular one for handshake
 */
open class MessageHandshakeDispatcher : MessageDispatcher(), HandshakeDispatcher {

    private val handshakeDispatcher = MessageDispatcher()

    override fun <T : Message> bind(type: KClass<T>, handler: MessageHandler<T>, handshake: Boolean) {
        return if(handshake) handshakeDispatcher.bind(type, handler) else super.bind(type, handler)
    }

    inline fun <reified T : Message> bind(encoder: MessageHandler<T>, handshake: Boolean) {
        bind(T::class, encoder, handshake)
    }

    override fun <T : Message> get(type: KClass<T>, handshake: Boolean): MessageHandler<T>? {
        return if(handshake) super.get(type) else handshakeDispatcher.get(type)
    }

    override fun dispatch(ctx: ChannelHandlerContext, message: Message, handshake: Boolean) {
        if(handshake) super.dispatch(ctx, message) else handshakeDispatcher.dispatch(ctx, message)
    }

}