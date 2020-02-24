package world.gregs.hestia.core.network.codec.decode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.getSession
import world.gregs.hestia.core.network.packet.PacketReader

/**
 * Reads inbound data into [PacketReader] which are added to the pipeline
 */
abstract class PacketDecoder : ByteToMessageDecoder() {

    internal val logger = LoggerFactory.getLogger(this::class.java)!!

    private enum class DecoderState {
        READ_OPCODE,
        READ_LENGTH,
        READ_PAYLOAD
    }

    private var state = DecoderState.READ_OPCODE
    private var opcode = 0
    private var size = 0

    @Throws(IllegalStateException::class, IllegalArgumentException::class)
    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (state == DecoderState.READ_OPCODE) {
            if (!buf.isReadable) {
                return
            }

            //Read packet opcode
            opcode = readOpcode(buf)
            //Find packet size
            size = getSize(ctx, opcode) ?: throw IllegalArgumentException("Unknown opcode: $opcode")
            //Read length if variable byte or short (-1, -2)
            state = if (size < 0) DecoderState.READ_LENGTH else DecoderState.READ_PAYLOAD
        }

        if (state == DecoderState.READ_LENGTH) {
            if (buf.readableBytes() < if(size == -1) 1 else 2) {
                return
            }
            //Read the exact payload size
            size = when (size) {
                -1 -> buf.readUnsignedByte().toInt()
                -2 -> buf.readShort().toInt()
                else -> throw IllegalStateException("Illegal read size $size")
            }

            state = DecoderState.READ_PAYLOAD
        }

        if (state == DecoderState.READ_PAYLOAD) {
            if (buf.readableBytes() < size) {
                return
            }

            //Copy from unsafe buffer
            val buffer = buf.readBytes(size)

            //Reset state
            state = DecoderState.READ_OPCODE

            //Reset ping
            ctx.getSession().ping = System.currentTimeMillis()

            //Handle data
            out.add(PacketReader(opcode = opcode, buffer = buffer))
        }
    }

    /**
     * Returns the packet opcode
     * @param buf The buffer to read from
     * @return The packets opcode
     */
    open fun readOpcode(buf: ByteBuf): Int {
        return buf.readUnsignedByte().toInt()
    }

    /**
     * Returns the expected size of a packet
     * @param ctx Channel context
     * @param opcode The packet who's size to get
     * @return The expected size (if any)
     */
    abstract fun getSize(ctx: ChannelHandlerContext, opcode: Int): Int?
}