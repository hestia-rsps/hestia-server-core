package world.gregs.hestia.core.network.codec.debug

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.Codec
import world.gregs.hestia.core.network.codec.decode.SimplePacketDecoder

open class DebugPacketDecoder(codec: Codec) : SimplePacketDecoder(codec) {

    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        logger.debug("Decoding bytes ${`in`.readableBytes()}")
        super.decode(ctx, `in`, out)
    }

    override fun process(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        logger.debug("Processing data...")
        super.process(ctx, buf, out)
    }

    override fun getSize(ctx: ChannelHandlerContext, opcode: Int): Int? {
        logger.debug("Processing $opcode")
        return super.getSize(ctx, opcode)
    }

    override fun missingData(ctx: ChannelHandlerContext, buf: ByteBuf, opcode: Int, expected: Int) {
        logger.debug("Missing data: $opcode expected: $expected actual: ${buf.readableBytes()}.")
        super.missingData(ctx, buf, opcode, expected)
    }

    override fun missingSize(ctx: ChannelHandlerContext, buf: ByteBuf, opcode: Int, out: MutableList<Any>) {
        logger.debug("Missing size $opcode")
        super.missingSize(ctx, buf, opcode, out)
    }
}