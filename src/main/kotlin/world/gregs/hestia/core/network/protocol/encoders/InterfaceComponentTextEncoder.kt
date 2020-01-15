package world.gregs.hestia.core.network.protocol.encoders

import world.gregs.hestia.core.network.codec.message.MessageEncoder
import world.gregs.hestia.core.network.codec.packet.Packet
import world.gregs.hestia.core.network.codec.packet.PacketBuilder
import world.gregs.hestia.core.network.protocol.ClientOpcodes.INTERFACE_TEXT
import world.gregs.hestia.core.network.protocol.encoders.messages.InterfaceComponentText

class InterfaceComponentTextEncoder : MessageEncoder<InterfaceComponentText>() {

    override fun encode(builder: PacketBuilder, message: InterfaceComponentText) {
        val (hash, text) = message
        builder.apply {
            writeOpcode(INTERFACE_TEXT, Packet.Type.VAR_SHORT)
            writeInt(hash)
            writeString(text)
        }
    }

}