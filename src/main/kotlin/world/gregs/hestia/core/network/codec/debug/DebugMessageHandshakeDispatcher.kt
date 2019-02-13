package world.gregs.hestia.core.network.codec.debug

import io.netty.channel.ChannelHandlerContext
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.codec.MessageHandshakeDispatcher
import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.codec.message.MessageHandler
import kotlin.reflect.KClass

open class DebugMessageHandshakeDispatcher : MessageHandshakeDispatcher() {

    private val logger = LoggerFactory.getLogger(this::class.java)!!

    override fun <T : Message> bind(type: KClass<T>, handler: MessageHandler<T>, handshake: Boolean) {
        logger.info("Bind $type $handler $handshake")
        super.bind(type, handler, handshake)
    }

    override fun <T : Message> bind(type: KClass<T>, handler: MessageHandler<T>) {
        logger.info("Bind $type $handler")
        super.bind(type, handler)
    }

    override fun <T : Message> get(type: KClass<T>, handshake: Boolean): MessageHandler<T>? {
        logger.info("Get $type $handshake")
        return super.get(type, handshake)
    }

    override fun <T : Message> get(type: KClass<T>): MessageHandler<T>? {
        logger.info("Get $type")
        return super.get(type)
    }

    override fun dispatch(ctx: ChannelHandlerContext, message: Message, handshake: Boolean) {
        logger.info("Dispatch $message $handshake")
        super.dispatch(ctx, message, handshake)
    }

    override fun dispatch(ctx: ChannelHandlerContext, message: Message) {
        logger.info("Dispatch $message")
        super.dispatch(ctx, message)
    }
}
