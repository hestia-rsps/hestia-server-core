package world.gregs.hestia.core.network.protocol.encoders

import world.gregs.hestia.core.network.codec.message.MessageEncoder
import world.gregs.hestia.core.network.codec.packet.Packet
import world.gregs.hestia.core.network.codec.packet.PacketBuilder
import world.gregs.hestia.core.network.protocol.ClientOpcodes.FRIENDS_CHAT_UPDATE
import world.gregs.hestia.core.network.protocol.encoders.messages.FriendsChatDisconnect

class FriendsChatDisconnectEncoder : MessageEncoder<FriendsChatDisconnect>() {

    override fun encode(builder: PacketBuilder, message: FriendsChatDisconnect) {
        builder.writeOpcode(FRIENDS_CHAT_UPDATE, Packet.Type.VAR_SHORT)
    }

}