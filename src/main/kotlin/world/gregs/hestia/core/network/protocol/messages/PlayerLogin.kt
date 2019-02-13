package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Notification to the social-server that a player has logged into the game-server
 * @param name The players display name
 * @param entity The players entity id
 * @param client The players client index
 */
data class PlayerLogin(val name: String, val entity: Int, val client: Int): Message