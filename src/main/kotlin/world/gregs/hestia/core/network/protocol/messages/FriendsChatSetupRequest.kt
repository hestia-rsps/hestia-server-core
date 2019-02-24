package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Request to open the friends chat setup widget
 * @param entity The players entity id
 */
data class FriendsChatSetupRequest(val entity: Int): Message