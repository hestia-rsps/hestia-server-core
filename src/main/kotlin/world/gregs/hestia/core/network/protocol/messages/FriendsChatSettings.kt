package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Friends chat setup interface actions to change players friends chat settings
 * @param entity The players entity id
 * @param hash The interface id and component id combined
 * @param option The option selected
 */
data class FriendsChatSettings(val entity: Int, val hash: Int, val option: Int): Message