package world.gregs.hestia.core.network.protocol.encoders

import world.gregs.hestia.core.network.codec.message.MessageEncoder
import world.gregs.hestia.core.network.codec.packet.Packet
import world.gregs.hestia.core.network.codec.packet.PacketBuilder
import world.gregs.hestia.core.network.protocol.ClientOpcodes.WIDGET_TEXT
import world.gregs.hestia.core.network.protocol.encoders.messages.WidgetComponentText

class WidgetComponentTextEncoder : MessageEncoder<WidgetComponentText>() {

    override fun encode(builder: PacketBuilder, message: WidgetComponentText) {
        val (hash, text) = message
        builder.apply {
            writeOpcode(WIDGET_TEXT, Packet.Type.VAR_SHORT)
            writeInt(hash)
            writeString(text)
        }
    }

}