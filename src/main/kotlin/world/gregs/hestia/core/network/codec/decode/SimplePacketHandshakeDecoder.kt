package world.gregs.hestia.core.network.codec.decode

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.HandshakeCodec
import world.gregs.hestia.core.network.codec.message.MessageHandshake

/**
 * Decodes inbound bytes into [Packet]s
 * @param codec
 */
open class SimplePacketHandshakeDecoder(private val codec: HandshakeCodec, protected val handshake: MessageHandshake) : PacketDecoder() {

    override fun getSize(ctx: ChannelHandlerContext, opcode: Int): Int? {
        return codec.get(opcode, handshake.shook(ctx))?.size
    }

}