package world.gregs.hestia.core.network.codec.debug

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.HandshakeCodec
import world.gregs.hestia.core.network.codec.decode.SimplePacketHandshakeDecoder
import world.gregs.hestia.core.network.codec.message.MessageHandshake

open class DebugPacketHandshakeDecoder(codec: HandshakeCodec, handshake: MessageHandshake) : SimplePacketHandshakeDecoder(codec, handshake) {

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        logger.debug("Decoding bytes ${buf.readableBytes()}")
        super.decode(ctx, buf, out)
    }

    override fun readOpcode(buf: ByteBuf): Int {
        val opcode = super.readOpcode(buf)
        logger.debug("Read opcode $opcode")
        return opcode
    }

    override fun getSize(ctx: ChannelHandlerContext, opcode: Int): Int? {
        logger.debug("Processing $opcode")
        return super.getSize(ctx, opcode)
    }

}