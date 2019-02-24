package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Request to log into the game
 * Note: Client provides a lot of information, not all of it is known
 * @param session The game-server client session id
 * @param username The players username
 * @param password The password entered
 * @param mode The clients display mode (0 = full screen, 1 = fixed, 2 = resizable)
 * @param width The clients window screen width
 * @param height The clients window screen height
 * @param antialias The clients antialias level
 * @param settings Client parameters
 * @param affiliate Facebook login id
 * @param os Operating system type (?)
 * @param version Java version
 * @param vendor Java vendor
 * @param javaRelease Java release
 * @param javaVersion Java version
 * @param javaUpdate Java update
 * @param processors Computer processor count
 */
data class LoginGame(val session: Int, val username: String, val password: String, val mode: Int, val width: Int, val height: Int, val antialias: Int, val settings: String, val affiliate: Int, val os: Int, val version: Int, val vendor: Int, val javaRelease: Int, val javaVersion: Int, val javaUpdate: Int, val processors: Int): Message