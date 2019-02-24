package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.protocol.Details

/**
 * Details about the world sent to world-server for registration
 * @param details The worlds details
 */
data class WorldInfo(val details: Details): Message