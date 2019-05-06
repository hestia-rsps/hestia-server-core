package world.gregs.hestia.core.network.codec.packet

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

class PacketReader(override var opcode: Int = -1, override val type: Packet.Type = Packet.Type.STANDARD, override val buffer: ByteBuf) : Packet {

    constructor(packet: Packet) : this(packet.opcode, packet.type, packet.buffer.discardReadBytes())

    constructor(array: ByteArray) : this(buffer = Unpooled.wrappedBuffer(array))

    override val length: Int = buffer.readableBytes()
    private var bitIndex = 0

    override fun readSmart(): Int {
        val peek = readUnsignedByte()
        return if (peek < 128) {
            peek and 0xFF
        } else {
            buffer.readerIndex(buffer.readerIndex() - 1)
            readUnsignedShort() - 32768
        }
    }

    override fun readBigSmart(): Int {
        val peek = buffer.getByte(buffer.readerIndex()).toInt()
        return if (peek < -2) {
            readInt() and 0x7fffffff
        } else {
            val value = readShort()
            if (value == 32767) {
                -1
            } else {
                value
            }
        }
    }

    override fun readLargeSmart(): Int {
        var baseValue = 0
        var lastValue = readSmart()
        while (lastValue == 32767) {
            lastValue = readSmart()
            baseValue += 32767
        }
        return baseValue + lastValue
    }

    override fun readLong(): Long {
        val first = readInt().toLong() and 0xffffffffL
        val second = readInt().toLong() and 0xffffffffL
        return second + (first shl 32)
    }

    override fun readString(): String {
        val sb = StringBuilder()
        var b: Int
        while (buffer.isReadable) {
            b = readByte()
            if (b == 0) {
                break
            }
            sb.append(b.toChar())
        }
        return sb.toString()
    }

    override fun readBytes(value: ByteArray) {
        buffer.readBytes(value)
    }

    override fun readBytes(array: ByteArray, offset: Int, length: Int) {
        buffer.readBytes(array, offset, length)
    }

    override fun release() {
        buffer.release()
    }

    override fun retain() {
        buffer.retain()
    }

    override fun skip(amount: Int) {
        buffer.skipBytes(amount)
    }

    override fun readableBytes(): Int {
        return buffer.readableBytes()
    }

    override fun resetReader() {
        buffer.resetReaderIndex()
    }

    override fun resetWriter() {
        buffer.resetWriterIndex()
    }

    override fun markReader() {
        buffer.markReaderIndex()
    }

    override fun markWriter() {
        buffer.markWriterIndex()
    }

    override fun reader(): Int {
        return buffer.readerIndex()
    }

    override fun writer(): Int {
        return buffer.writerIndex()
    }

    override fun readSigned(type: DataType, modifier: Modifier, order: Endian): Long {
        var longValue = read(type, modifier, order)
        if (type != DataType.LONG) {
            val max = Math.pow(2.0, type.length * 8.0 - 1).toInt()
            if (longValue > max - 1) {
                longValue -= max * 2L
            }
        }
        return longValue
    }

    override fun readUnsigned(type: DataType, modifier: Modifier, order: Endian): Long {
        if (type == DataType.LONG) {
            throw IllegalArgumentException("Longs must be signed")
        }
        val longValue = read(type, modifier, order)
        return longValue and -0x1L
    }

    /**
     * Reads [type] number of bytes with [modifier] and [order]
     * @param type The byte type to read
     * @param modifier The first byte read modifier
     * @param order The endianness
     * @return The read value
     */
    private fun read(type: DataType, modifier: Modifier, order: Endian): Long {
        //Check bytes are available
        if (!buffer.isReadable(type.length)) {
            throw IndexOutOfBoundsException("Not enough allocated buffer remaining $type.")
        }

        var longValue: Long = 0
        when (order) {
            Endian.BIG, Endian.LITTLE -> {
                //For by length
                val range = if (order == Endian.LITTLE) 0 until type.length else type.length - 1 downTo 0
                var read: Long
                for (i in range) {
                    //If first and has a modifier
                    read = if (i == 0 && modifier != Modifier.NONE) {
                                //Read with variable modifier transform
                                when (modifier) {
                                    Modifier.ADD -> buffer.readByte() - 128
                                    Modifier.INVERSE -> -buffer.readByte()
                                    Modifier.SUBTRACT -> 128 - buffer.readByte()
                                    else -> throw IllegalArgumentException("Unknown byte modifier")
                                } and 0xFF
                            } else {
                                //Read with position shift
                                buffer.readByte().toInt() and 0xFF shl i * 8
                            }.toLong()
                    longValue = longValue or read
                }
            }
            Endian.MIDDLE -> {
                if (type != DataType.INT) {
                    throw IllegalArgumentException("Middle endian can only be used with an integer")
                }

                if (modifier != Modifier.NONE && modifier != Modifier.INVERSE) {
                    throw IllegalArgumentException("Middle endian doesn't support variable modifier $modifier")
                }

                val range = listOf(8, 0, 24, 16)
                //Reverse range if inverse modifier
                for (i in if (modifier == Modifier.NONE) range else range.reversed()) {
                    longValue = longValue or (buffer.readByte().toInt() and 0xFF shl i).toLong()
                }
            }
        }
        return longValue
    }

    override fun startBitAccess(): Packet {
        bitIndex = buffer.readerIndex() * 8
        return this
    }

    override fun finishBitAccess(): Packet {
        buffer.readerIndex((bitIndex + 7) / 8)
        return this
    }

    @Suppress("NAME_SHADOWING")
    override fun readBits(bitCount: Int): Int {
        if (bitCount < 0 || bitCount > 32) {
            throw IllegalArgumentException("Number of bits must be between 1 and 32 inclusive")
        }

        var bitCount = bitCount
        var bytePos = bitIndex shr 3
        var bitOffset = 8 - (bitIndex and 7)
        var value = 0
        bitIndex += bitCount

        while (bitCount > bitOffset) {
            value += buffer.getByte(bytePos++).toInt() and BIT_MASKS[bitOffset] shl bitCount - bitOffset
            bitCount -= bitOffset
            bitOffset = 8
        }
        value += if (bitCount == bitOffset) {
            buffer.getByte(bytePos).toInt() and BIT_MASKS[bitOffset]
        } else {
            buffer.getByte(bytePos).toInt() shr bitOffset - bitCount and BIT_MASKS[bitCount]
        }
        return value
    }

    companion object {
        /**
         * Bit masks for [readBits]
         */
        private val BIT_MASKS = IntArray(32)

        init {
            for (i in BIT_MASKS.indices)
                BIT_MASKS[i] = (1 shl i) - 1
        }
    }
}