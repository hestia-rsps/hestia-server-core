package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Notification that the world is now registered on the world-server
 * @param world The allocated world id
 */
data class SocialDetails(val world: Int): Message