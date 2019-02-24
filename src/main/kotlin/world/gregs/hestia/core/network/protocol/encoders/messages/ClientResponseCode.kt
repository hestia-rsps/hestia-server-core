package world.gregs.hestia.core.network.protocol.encoders.messages

import world.gregs.hestia.core.network.codec.message.Message

data class ClientResponseCode(val response: Int) : Message