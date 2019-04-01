package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Notification that the players login request was authorised
 * @param session The players session id
 * @param name The players display name
 * @param isaacKeys The isaac encryption keys
 * @param mode The clients display mode (0 = full screen, 1 = fixed, 2 = resizable)
 * @param width The clients window screen width
 * @param height The clients window screen height
 */
data class PlayerLoginSuccess(val session: Int, val name: String, val isaacKeys: IntArray, val mode: Int, val width: Int, val height: Int): Message {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerLoginSuccess

        if (session != other.session) return false
        if (name != other.name) return false
        if (!isaacKeys.contentEquals(other.isaacKeys)) return false
        if (mode != other.mode) return false
        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }

    override fun hashCode(): Int {
        var result = session
        result = 31 * result + name.hashCode()
        result = 31 * result + isaacKeys.contentHashCode()
        result = 31 * result + mode
        result = 31 * result + width
        result = 31 * result + height
        return result
    }
}