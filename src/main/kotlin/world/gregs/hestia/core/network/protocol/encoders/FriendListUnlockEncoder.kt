package world.gregs.hestia.core.network.protocol.encoders

import world.gregs.hestia.core.network.codec.message.MessageEncoder
import world.gregs.hestia.core.network.codec.packet.Packet
import world.gregs.hestia.core.network.codec.packet.PacketBuilder
import world.gregs.hestia.core.network.protocol.ClientOpcodes.FRIEND_LIST
import world.gregs.hestia.core.network.protocol.encoders.messages.FriendListUnlock

class FriendListUnlockEncoder : MessageEncoder<FriendListUnlock>() {

    override fun encode(builder: PacketBuilder, message: FriendListUnlock) {
        builder.writeOpcode(FRIEND_LIST, Packet.Type.VAR_SHORT)
    }

}