package world.gregs.hestia.core.network.codec.decode

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.cache.crypto.Cipher
import world.gregs.hestia.core.network.codec.Codec
import world.gregs.hestia.core.network.codec.packet.Packet

/**
 * Decodes inbound bytes into [Packet]s
 * @param codec
 */
open class SimpleIsaacPacketDecoder(private val codec: Codec, cipher: Cipher) : IsaacPacketDecoder(cipher) {

    override fun getSize(ctx: ChannelHandlerContext, opcode: Int): Int? {
        return codec.get(opcode)?.size
    }

}