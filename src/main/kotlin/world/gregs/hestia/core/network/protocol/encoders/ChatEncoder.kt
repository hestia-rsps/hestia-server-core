package world.gregs.hestia.core.network.protocol.encoders

import world.gregs.hestia.core.network.codec.message.MessageEncoder
import world.gregs.hestia.core.network.protocol.ClientOpcodes.CHAT
import world.gregs.hestia.core.network.protocol.encoders.messages.Chat
import world.gregs.hestia.core.services.formatName
import world.gregs.hestia.core.network.packet.Packet
import world.gregs.hestia.core.network.packet.PacketBuilder

class ChatEncoder : MessageEncoder<Chat>() {

    override fun encode(builder: PacketBuilder, message: Chat) {
        val (type, tile, name, text) = message
        var mask = 0
        if (name != null) {
            mask = mask or 0x1
            if (name != name.formatName()) {
                mask = mask or 0x2
            }
        }
        builder.apply {
            writeOpcode(CHAT, Packet.Type.VAR_BYTE)
            writeSmart(type)
            writeInt(tile)//Tile position hash = y + (x << 14) + (plane << 28)
            writeByte(mask)
            if (name != null) {
                writeString(name)
                if (mask and 0x2 == 0x2) {
                    writeString(name.formatName())
                }
            }
            writeString(text)
        }
    }

}