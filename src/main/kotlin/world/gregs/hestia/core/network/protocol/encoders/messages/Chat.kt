package world.gregs.hestia.core.network.protocol.encoders.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * A chat box message to display
 * @param type The message type
 * @see [type](https://github.com/hestia-rsps/hestia/wiki/message-types)
 * @param tile The tile the message was sent from
 * @param name Optional display name?
 * @param message The chat message text
 */
data class Chat(val type: Int, val tile: Int, val name: String?, val message: String) : Message