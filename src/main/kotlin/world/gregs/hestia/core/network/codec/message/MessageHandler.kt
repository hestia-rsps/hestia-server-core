package world.gregs.hestia.core.network.codec.message

import io.netty.channel.ChannelHandlerContext

/**
 * Handles all [Message] of type [T]
 */
interface MessageHandler<T : Message> {

    /**
     * Handles data from a message, potentially adds a new message to the pipeline
     * @param ctx The channel the message is from
     * @param message The message
     */
    fun handle(ctx: ChannelHandlerContext, message: T)

}