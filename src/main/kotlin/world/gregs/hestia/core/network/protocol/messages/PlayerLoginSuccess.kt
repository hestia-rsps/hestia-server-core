package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Notification that the players login request was authorised
 * @param session The players session id
 * @param name The players display name
 * @param mode The clients display mode (0 = full screen, 1 = fixed, 2 = resizable)
 * @param width The clients window screen width
 * @param height The clients window screen height
 */
data class PlayerLoginSuccess(val session: Int, val name: String, val mode: Int, val width: Int, val height: Int): Message