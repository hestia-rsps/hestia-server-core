package world.gregs.hestia.core.network.codec.debug

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.cache.crypto.Cipher
import world.gregs.hestia.core.network.codec.HandshakeCodec
import world.gregs.hestia.core.network.codec.decode.SimpleIsaacPacketHandshakeDecoder
import world.gregs.hestia.core.network.codec.message.MessageHandshake

open class DebugIsaacPacketHandshakeDecoder(codec: HandshakeCodec, handshake: MessageHandshake, cipher: Cipher) : SimpleIsaacPacketHandshakeDecoder(codec, handshake, cipher) {

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
        logger.debug("Processing $opcode $cipher")
        return super.getSize(ctx, opcode)
    }

}