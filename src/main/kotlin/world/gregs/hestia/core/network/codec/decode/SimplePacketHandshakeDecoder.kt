package world.gregs.hestia.core.network.codec.decode

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.HandshakeCodec
import world.gregs.hestia.core.network.codec.message.MessageHandshake
import world.gregs.hestia.core.network.codec.packet.Packet

/**
 * Decodes inbound bytes into [Packet]s
 * @param codec
 */
open class SimplePacketHandshakeDecoder(private val codec: HandshakeCodec, handshake: MessageHandshake) : PacketHandshakeDecoder(handshake) {

    override fun getSize(ctx: ChannelHandlerContext, opcode: Int): Int? {
        return codec.get(opcode, handshake.shook(ctx))?.size
    }

}