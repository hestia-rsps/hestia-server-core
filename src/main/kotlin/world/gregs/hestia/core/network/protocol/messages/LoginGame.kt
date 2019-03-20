package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Request to log into the game
 * Note: Client provides a lot of information, not all of it is known
 * @param session The game-server client session id
 * @param username The players username
 * @param password The password entered
 * @param isaacKeys The isaac encryption keys
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
 * @param processors Computer processor thread count
 */
data class LoginGame(val session: Int, val username: String, val password: String, val isaacKeys: IntArray, val mode: Int, val width: Int, val height: Int, val antialias: Int, val settings: String, val affiliate: Int, val os: Int, val version: Int, val vendor: Int, val javaRelease: Int, val javaVersion: Int, val javaUpdate: Int, val processors: Int): Message {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginGame

        if (session != other.session) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (!isaacKeys.contentEquals(other.isaacKeys)) return false
        if (mode != other.mode) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (antialias != other.antialias) return false
        if (settings != other.settings) return false
        if (affiliate != other.affiliate) return false
        if (os != other.os) return false
        if (version != other.version) return false
        if (vendor != other.vendor) return false
        if (javaRelease != other.javaRelease) return false
        if (javaVersion != other.javaVersion) return false
        if (javaUpdate != other.javaUpdate) return false
        if (processors != other.processors) return false

        return true
    }

    override fun hashCode(): Int {
        var result = session
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + isaacKeys.contentHashCode()
        result = 31 * result + mode
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + antialias
        result = 31 * result + settings.hashCode()
        result = 31 * result + affiliate
        result = 31 * result + os
        result = 31 * result + version
        result = 31 * result + vendor
        result = 31 * result + javaRelease
        result = 31 * result + javaVersion
        result = 31 * result + javaUpdate
        result = 31 * result + processors
        return result
    }
}