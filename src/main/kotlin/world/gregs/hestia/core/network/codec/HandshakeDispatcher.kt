package world.gregs.hestia.core.network.codec

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.codec.message.MessageHandler
import kotlin.reflect.KClass

/**
 * Handles a main codec and a handshake codec
 */
interface HandshakeDispatcher {

    /**
     * Binds a handler by message class
     * @param type The [Message] type
     * @param handler The handler to add
     * @param handshake Whether to bind to the handshake dispatcher
     */
    fun <T : Message> bind(type: KClass<T>, handler: MessageHandler<T>, handshake: Boolean)

    /**
     * Find a handler by message class
     * @param type The [Message] type
     * @param handshake Whether handshake is complete
     */
    fun <T : Message> get(type: KClass<T>, handshake: Boolean): MessageHandler<T>?

    /**
     * Dispatches a message to a handler for that message type
     * @param ctx The channel
     * @param message The message to handle
     * @param handshake Whether handshake is complete
     */
    fun dispatch(ctx: ChannelHandlerContext, message: Message, handshake: Boolean)

}