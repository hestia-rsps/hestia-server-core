package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Notification that a player logged out of the game
 * @param entity The players entity id
 */
data class PlayerLogout(val entity: Int): Message