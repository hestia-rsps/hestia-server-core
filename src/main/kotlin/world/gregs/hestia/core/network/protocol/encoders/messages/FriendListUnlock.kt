package world.gregs.hestia.core.network.protocol.encoders.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Sends an empty friend list to remove the "Connecting to friend server" message
 */
class FriendListUnlock : Message