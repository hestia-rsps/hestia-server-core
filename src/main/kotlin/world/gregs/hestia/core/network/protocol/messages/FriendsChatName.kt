package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Request from game-server for social-server to rename players friends chat
 * @param entity The players entity id
 * @param name The friends chat name
 */
data class FriendsChatName(val entity: Int, val name: String): Message