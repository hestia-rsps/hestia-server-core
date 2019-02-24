package world.gregs.hestia.core.network.protocol.encoders

import world.gregs.hestia.core.network.codec.message.MessageEncoder
import world.gregs.hestia.core.network.codec.packet.PacketBuilder
import world.gregs.hestia.core.network.protocol.encoders.messages.ClientResponseCode

class ClientResponseEncoder : MessageEncoder<ClientResponseCode>() {

    override fun encode(builder: PacketBuilder, message: ClientResponseCode) {
        builder.writeOpcode(message.response)
    }

}