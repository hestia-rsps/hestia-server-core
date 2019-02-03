package world.gregs.hestia.core.network.codec.decode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.packets.Packet

abstract class PacketDecoder : Decoder() {

    abstract fun getSize(opcode: Int): Int?

    override fun process(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        //Read all complete packets
        while (buf.isReadable && ctx.channel().isOpen) {
            //Read packet opcode
            val opcode = buf.readUnsignedByte().toInt()
            //Find packet size
            val size = getSize(opcode)

            if(size == null) {
                missingSize(buf, opcode, out)
                break
            }

            //Calculate expected size
            val expectedSize = when (size) {
                -1 -> buf.readUnsignedByte().toInt()
                -2 -> buf.readShort().toInt()
                else -> size
            }

            //Check size is available
            if (!buf.isReadable(expectedSize)) {
                missingData(ctx, buf)
                return
            }

            //Copy from unsafe buffer
            val buffer = buf.readBytes(expectedSize)

            //Mark beginning of next packet
            buf.markReaderIndex()

            //Handle data
            out.add(Packet(opcode = opcode, buffer = buffer))
        }
    }

    /**
     * Handles action when there is less data than size
     */
    open fun missingData(ctx: ChannelHandlerContext, buf: ByteBuf) {
        //Byte default handle fragmented data: reset the reader and allow buffer to accumulate more data
        buf.resetReaderIndex()
    }

    /**
     * Handles when a packet doesn't have a specified size
     */
    open fun missingSize(buf: ByteBuf, opcode: Int, out: MutableList<Any>) {
        //Clears buffer to stop accumulation
        logger.warn("Unhandled packet: $opcode")
        buf.skipBytes(buf.readableBytes())
        out.add(0)
    }
}