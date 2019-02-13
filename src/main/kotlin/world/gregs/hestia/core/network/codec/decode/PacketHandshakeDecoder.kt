package world.gregs.hestia.core.network.codec.decode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.message.MessageHandshake

/**
 * If handshake isn't complete other packets are discarded
 */
abstract class PacketHandshakeDecoder(protected val handshake: MessageHandshake) : PacketDecoder() {

    override fun missingSize(ctx: ChannelHandlerContext, buf: ByteBuf, opcode: Int, out: MutableList<Any>) {
        if(handshake.shook(ctx)) {
            super.missingSize(ctx, buf, opcode, out)
        } else {
            //Clear all of the current data
            buf.skipBytes(buf.readableBytes())
            //Remove read bytes
            out.add(13)
        }
    }
}