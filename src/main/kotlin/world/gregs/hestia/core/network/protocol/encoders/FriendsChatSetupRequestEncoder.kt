package world.gregs.hestia.core.network.protocol.encoders

import world.gregs.hestia.core.network.codec.message.MessageEncoder
import world.gregs.hestia.core.network.protocol.WorldOpcodes.FRIENDS_CHAT_SETUP
import world.gregs.hestia.core.network.protocol.messages.FriendsChatSetupRequest
import world.gregs.hestia.core.network.packet.PacketBuilder

class FriendsChatSetupRequestEncoder : MessageEncoder<FriendsChatSetupRequest>() {

    override fun encode(builder: PacketBuilder, message: FriendsChatSetupRequest) {
        val (entity) = message
        builder.apply {
            writeOpcode(FRIENDS_CHAT_SETUP)
            writeInt(entity)
        }
    }

}