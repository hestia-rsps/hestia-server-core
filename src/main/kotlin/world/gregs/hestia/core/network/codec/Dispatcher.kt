package world.gregs.hestia.core.network.codec

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.codec.message.MessageHandler
import kotlin.reflect.KClass

/**
 * Stores handlers and dispatches messages to them
 */
interface Dispatcher {

    /**
     * Binds a handler by message class
     * @param type The [Message] type
     * @param handler The handler to add
     */
    fun <T : Message> bind(type: KClass<T>, handler: MessageHandler<T>)

    /**
     * Find a handler by message class
     * @param type The [Message] type
     */
    fun <T : Message> get(type: KClass<T>): MessageHandler<T>?

    /**
     * Dispatches a message to a handler for that message type
     * @param ctx The channel
     * @param message The message to handle
     */
    fun dispatch(ctx: ChannelHandlerContext, message: Message)

}