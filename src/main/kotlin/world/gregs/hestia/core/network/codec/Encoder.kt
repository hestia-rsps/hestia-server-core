package world.gregs.hestia.core.network.codec

import world.gregs.hestia.core.network.packets.Packet
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder

@ChannelHandler.Sharable
class Encoder : MessageToMessageEncoder<Packet>() {

    override fun encode(context: ChannelHandlerContext?, packet: Packet?, out: MutableList<Any>?) {
        if (packet == null)
            return

        if (packet.hasOpcode) {
            val response = Unpooled.buffer(packet.length + packet.type.ordinal + 1)

            //writeSmart
            if (packet.opcode >= 128) {
                response.writeShort(packet.opcode + 32768)
            } else {
                response.writeByte(packet.opcode)
            }

            when (packet.type) {
                Packet.Type.VAR_BYTE -> response.writeByte(packet.length)
                Packet.Type.VAR_SHORT -> response.writeShort(packet.length)
                else -> {
                }
            }

            response.writeBytes(packet.buffer)
            out?.add(response)
        } else {
            out?.add(packet.buffer)
        }
    }
}