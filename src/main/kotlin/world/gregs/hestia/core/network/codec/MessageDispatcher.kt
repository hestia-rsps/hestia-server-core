package world.gregs.hestia.core.network.codec

import io.netty.channel.ChannelHandlerContext
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.codec.message.MessageHandler
import kotlin.reflect.KClass

/**
 * Implementation of [Dispatcher]
 * Stores [MessageHandler]'s and handles dispatching [Message]'s to them
 */
@Suppress("UNCHECKED_CAST")
open class MessageDispatcher : Dispatcher {

    private val handlers = HashMap<KClass<*>, MessageHandler<*>>()
    private val logger = LoggerFactory.getLogger(MessageDispatcher::class.java)!!

    override fun <T : Message> bind(type: KClass<T>, handler: MessageHandler<T>) {
        handlers[type] = handler
    }

    inline fun <reified T : Message> bind(encoder: MessageHandler<T>) {
        bind(T::class, encoder)
    }

    override fun <T : Message> get(type: KClass<T>): MessageHandler<T>? {
        return handlers[type] as? MessageHandler<T>
    }

    override fun dispatch(ctx: ChannelHandlerContext, message: Message) {
        val handler = get(message::class) as? MessageHandler<Message>
                ?: run {
                    logger.warn("Unhandled message: $message")
                    return
                }

        try {
            handler.handle(ctx, message)
        } catch (t: Throwable) {
            logger.warn("Error processing message: ", t)
        }
    }

}