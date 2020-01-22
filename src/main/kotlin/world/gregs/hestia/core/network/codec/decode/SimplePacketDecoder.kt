package world.gregs.hestia.core.network.codec.decode

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.Codec

/**
 * Decodes inbound bytes into [Packet]s
 * @param codec
 */
open class SimplePacketDecoder(private val codec: Codec) : PacketDecoder() {

    override fun getSize(ctx: ChannelHandlerContext, opcode: Int): Int? {
        return codec.get(opcode)?.size
    }

}