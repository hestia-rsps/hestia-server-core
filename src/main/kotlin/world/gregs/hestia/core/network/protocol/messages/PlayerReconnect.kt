package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Notification that a player is still connected to the game-server when social server reconnects
 * @param name The players display name
 * @param entity The players entity id
 * @param client The players client index
 */
data class PlayerReconnect(val name: String, val entity: Int, val client: Int): Message