package world.gregs.hestia.core.network.protocol.encoders

import world.gregs.hestia.core.network.codec.message.MessageEncoder
import world.gregs.hestia.core.network.protocol.ClientOpcodes.INTERFACE_TEXT
import world.gregs.hestia.core.network.protocol.encoders.messages.InterfaceComponentText
import world.gregs.hestia.core.network.packet.Packet
import world.gregs.hestia.core.network.packet.PacketBuilder

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