package world.gregs.hestia.core.network.codec.decode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.getSession

abstract class HandshakeDecoder : PacketDecoder() {

    override fun missingData(ctx: ChannelHandlerContext, buf: ByteBuf) {
        logger.info("Invalid size: ${buf.readableBytes()}")
        ctx.getSession().close()
    }

    override fun missingSize(buf: ByteBuf, opcode: Int, out: MutableList<Any>) {
        //Reset to before opcode was read
        buf.resetReaderIndex()
        //Process normally (not a packet)
        out.add(buf.readBytes(buf.readableBytes()))
    }
}