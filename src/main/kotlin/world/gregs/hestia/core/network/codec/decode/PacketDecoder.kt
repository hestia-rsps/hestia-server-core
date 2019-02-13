package world.gregs.hestia.core.network.codec.decode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.packet.PacketReader
import world.gregs.hestia.core.network.getSession

/**
 * Checks inbound data forms complete packets before processing
 */
abstract class PacketDecoder : ByteDecoder() {

    /**
     * Processes [buf] adds packets to [out] when they are complete
     * If expected size is greater than the amount of data available then [missingData] is called
     * If there is no known information on the packet then [missingSize] is called
     */
    override fun process(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        //Read all complete packets
        while (buf.isReadable && ctx.channel().isOpen) {
            //Read packet opcode
            val opcode = buf.readUnsignedByte().toInt()
            //Find packet size
            val size = getSize(ctx, opcode)

            if (size == null) {
                missingSize(ctx, buf, opcode, out)
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
                missingData(ctx, buf, opcode, expectedSize)
                return
            }

            //Copy from unsafe buffer
            val buffer = buf.readBytes(expectedSize)

            //Mark beginning of next packet
            buf.markReaderIndex()

            //Reset ping
            ctx.getSession().ping = System.currentTimeMillis()

            //Handle data
            out.add(PacketReader(opcode = opcode, buffer = buffer))
        }
    }

    /**
     * Returns the expected size of a packet
     * @param opcode The packet who's size to get
     * @return The expected size (if any)
     */
    abstract fun getSize(ctx: ChannelHandlerContext, opcode: Int): Int?

    /**
     * Handles action when there is less data than size
     */
    open fun missingData(ctx: ChannelHandlerContext, buf: ByteBuf, opcode: Int, expected: Int) {
        //Byte default handle fragmented data: reset the reader and allow buffer to accumulate more data
        buf.resetReaderIndex()
    }

    /**
     * Handles when a packet doesn't have a specified size
     */
    open fun missingSize(ctx: ChannelHandlerContext, buf: ByteBuf, opcode: Int, out: MutableList<Any>) {
        //Clears buffer to stop accumulation
        logger.warn("Unhandled packet: $opcode ${buf.readableBytes()}")
        buf.skipBytes(buf.readableBytes())
        out.add(11)
    }
}