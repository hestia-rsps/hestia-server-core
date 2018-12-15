package world.gregs.hestia.core.network.packets


import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import world.gregs.hestia.core.network.packets.Packet.Type

/**
 * A unit of byte data
 * @param opcode operation code
 * @param type [Type]
 * @param buffer [ByteBuf]
 */
class Packet(var opcode: Int = -1, val type: Type = Type.STANDARD, val buffer: ByteBuf) {

    constructor(array: ByteArray) : this(buffer = Unpooled.wrappedBuffer(array))

    val length: Int

    val hasOpcode: Boolean
        get() = opcode > 0

    init {
        Companion.type = type
        this.length = buffer.readableBytes()
    }

    /**
     * Reads a byte.
     * @return [Byte]
     */
    fun readByte(): Int {
        return if (buffer.isReadable(1)) buffer.readByte().toInt() else 0
    }

    /**
     * Reads an unsigned byte.
     * @return [Short]
     */
    fun readUnsignedByte(): Int {
        return if (buffer.isReadable(1)) buffer.readUnsignedByte().toInt() else 0
    }

    /**
     * Reads a type A byte.
     * @return [Byte]
     */
    fun readByteA(): Int {
        return (readByte() - 128).toByte().toInt()
    }

    /**
     * Reads a type C byte.
     * @return [Byte]
     */
    fun readByteC(): Int {
        return -readByte()
    }

    /**
     * Reads a type S byte.
     * @return [Byte]
     */
    fun readByteS(): Int {
        return (128 - readByte()).toByte().toInt()
    }

    /**
     * Reads all bytes into [ByteArray]
     * @param value The array to be written to.
     */
    fun readBytes(value: ByteArray) {
        buffer.readBytes(value)
    }

    /**
     * Reads [length] number of bytes starting at [offset] to [array].
     * @param array The [ByteArray] to be written to
     * @param offset Destination index
     * @param length Number of bytes to read
     */
    fun readBytes(array: ByteArray, offset: Int, length: Int = array.size) {
        buffer.readBytes(array, offset, length)
    }

    /**
     * Reads a short.
     * @return [Short]
     */
    fun readShort(): Int {
        return if (buffer.isReadable(2)) buffer.readShort().toInt() else 0
    }

    /**
     * Reads an unsigned short.
     * @return [Int]
     */
    fun readUnsignedShort(): Int {
        return if (buffer.isReadable(2)) buffer.readUnsignedShort() else 0
    }

    /**
     * Reads a type A short.
     * @return [Int]
     */
    fun readShortA(): Int {
        return readByte() and 0xFF shl 8 or (readByte() - 128 and 0xFF)
    }

    /**
     * Reads a little-endian short.
     * @return [Int]
     */
    fun readLEShort(): Int {
        return readByte() and 0xFF or (readByte() and 0xFF shl 8)
    }

    /**
     * Reads a little-endian type A short.
     * @return [Int]
     */
    fun readLEShortA(): Int {
        return readByte() - 128 and 0xFF or (readByte() and 0xFF shl 8)
    }

    /**
     * Reads a 3-byte integer.
     * @return [Int]
     */
    fun readTriByte(): Int {
        return readUnsignedByte() shl 16 or (readUnsignedByte() shl 8) or readUnsignedByte()
    }

    /**
     * Reads a integer.
     * @return [Int]
     */
    fun readInt(): Int {
        return if (buffer.isReadable(4)) buffer.readInt() else 0
    }

    /**
     * Reads a long-endian integer.
     * @return [Int]
     */
    fun readLEInt(): Int {
        return readUnsignedByte() + (readUnsignedByte() shl 8) + (readUnsignedByte() shl 16) + (readUnsignedByte() shl 24)
    }

    /**
     * Reads a type 1 integer.
     * @return [Int]
     */
    fun readInt1(): Int {
        return (readUnsignedByte() shl 8) + readUnsignedByte() + (readUnsignedByte() shl 24) + (readUnsignedByte() shl 16)
    }

    /**
     * Reads a type 2 integer.
     * @return [Int]
     */
    fun readInt2(): Int {
        return (readUnsignedByte() shl 16) + (readUnsignedByte() shl 24) + readUnsignedByte() + (readUnsignedByte() shl 8)
    }

    /**
     * Reads a smart
     * @return [Int]
     */
    fun readSmart(): Int {
        val peek = readByte()
        buffer.readerIndex(buffer.readerIndex() - 1)
        return if (peek < 128) {
            readByte() and 0xFF
        } else {
            (readShort() and 0xFFFF) - 32768
        }
    }

    fun readUnsignedSmart(): Int {
        buffer.markReaderIndex()
        val i = 0xff and readByte()
        buffer.resetReaderIndex()
        return if (i >= 128) {
            -32768 + this.readUnsignedShort()
        } else {
            readUnsignedByte()
        }
    }

    /**
     * Reads a smart
     * @return [Int]
     */
    fun readBigSmart(): Int {
        val peek = readByte()
        buffer.readerIndex(buffer.readerIndex() - 1)
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

    /**
     * Reads a smart
     * @return [Int]
     */
    fun readSmart2(): Int {
        var baseValue = 0
        var lastValue = readUnsignedSmart()
        while (lastValue == -32767) {
            lastValue = readUnsignedSmart()
            baseValue += 32767
        }
        return baseValue + lastValue
    }

    /**
     * Reads a long.
     * @return [Long]
     */
    fun readLong(): Long {
        return if (buffer.isReadable(6)) buffer.readLong() else 0
    }

    /**
     * Reads a string.
     * @return [String]
     */
    fun readString(): String {
        val sb = StringBuilder()
        var b: Int
        while (buffer.isReadable) {
            b = readByte()
            if (b == 0)
                break
            sb.append(b.toChar())
        }
        return sb.toString()
    }

    fun readJagString(): String {
        readByte()
        return readString()
    }


    /**
     * Skips the [amount] bytes.
     * @param amount Number of bytes to skip
     */
    fun skip(amount: Int) {
        buffer.skipBytes(amount)
    }

    /**
     * Returns the remaining number of readable bytes.
     * @return [Int]
     */
    fun readableBytes(): Int {
        return buffer.readableBytes()
    }

    enum class Type {
        STANDARD,
        VAR_BYTE,
        VAR_SHORT;
    }

    /**
     * Used to instantiate a [Packet]
     *
     * @param opcode packet opcode
     * @param type [PacketType]
     */
    open class Builder(private var opcode: Int = -1, private val type: Type = Type.STANDARD) {

        val buffer: ByteBuf = Unpooled.buffer()

        private var bitPosition = 0


        /**
         * Writes a byte to [buffer].
         * @param value [Int]
         */
        fun writeByte(value: Int): Builder {
            buffer.writeByte(value)
            return this
        }

        /**
         * Writes a byte to [buffer].
         * @param value [Int]
         */
        fun writeByte(value: Byte): Builder {
            return writeByte(value.toInt())
        }

        /**
         * Writes a A type [Byte] to [buffer].
         * @param value [Int]
         */
        fun writeByteA(value: Int): Builder {
            writeByte((value + 128).toByte())
            return this
        }

        /**
         * Writes a Type C [Byte] to [buffer]
         * @param value [Int]
         */
        fun writeByteC(value: Int): Builder {
            writeByte((-value).toByte())
            return this
        }

        /**
         * Writes a type S [Byte] to [buffer]
         * @param value [Byte]
         */
        fun writeByteS(value: Int): Builder {
            writeByte((128 - value).toByte())
            return this
        }

        /**
         * Writes a [ByteArray] to [buffer].
         * @param value [ByteArray]
         */
        fun writeBytes(value: ByteArray): Builder {
            buffer.writeBytes(value)
            return this
        }

        /**
         * Writes a [ByteBuf] to [buffer].
         * @param value [ByteBuf]
         */
        fun writeBytes(value: ByteBuf): Builder {
            buffer.writeBytes(value)
            return this
        }

        /**
         * Writes part of a [ByteArray] to [buffer] at position
         * @param data [ByteArray] to be read from
         * @param offset Index to start writing to
         * @param length Amount of array to be written
         */
        fun writeBytes(data: ByteArray, offset: Int, length: Int): Builder {
            buffer.writeBytes(data, offset, length)
            return this
        }

        /**
         * Writes a Smart to [buffer]
         * @param value [Int]
         */
        fun writeSmart(value: Int): Builder {
            if (value >= 128) {
                writeShort(value + 32768)
            } else {
                writeByte(value.toByte())
            }
            return this
        }

        /**
         * Writes a [Short] to [buffer].
         * @param value [Short]
         */
        fun writeShort(value: Short): Builder {
            return writeShort(value.toInt())
        }

        /**
         * Writes a [Short] to [buffer].
         * @param value [Int]
         */
        fun writeShort(value: Int): Builder {
            buffer.writeShort(value)
            return this
        }

        /**
         * Writes a A type [Short] to [buffer].
         * @param value [Int]
         */
        fun writeShortA(value: Int): Builder {
            writeByte((value shr 8).toByte().toInt())
            writeByte((value + 128).toByte().toInt())
            return this
        }

        /**
         * Writes a little-endian [Short] to [buffer]
         * @param value [Short]
         */
        fun writeLEShort(value: Int): Builder {
            writeByte(value.toByte().toInt())
            writeByte((value shr 8).toByte().toInt())
            return this
        }

        /**
         * Writes a type A little-endian [Short] to [buffer].
         * @param value [Int]
         */
        fun writeLEShortA(value: Int): Builder {
            writeByte((value + 128).toByte().toInt())
            writeByte((value shr 8).toByte().toInt())
            return this
        }

        /**
         * Writes a Medium [Int] to [buffer]
         * @param value [Int]
         */
        fun writeMedium(value: Int): Builder {
            writeByte((value shr 16).toByte().toInt())
            writeByte((value shr 8).toByte().toInt())
            writeByte(value.toByte().toInt())
            return this
        }

        /**
         * Writes a [Int] to [buffer].
         * @param value [Int]
         */
        fun writeInt(value: Int): Builder {
            buffer.writeInt(value)
            return this
        }

        /**
         * Writes a Type 1 [Int] to [buffer]
         * @param value [Int]
         */
        fun writeInt1(value: Int): Builder {
            writeByte((value shr 8).toByte().toInt())
            writeByte(value.toByte().toInt())
            writeByte((value shr 24).toByte().toInt())
            writeByte((value shr 16).toByte().toInt())
            return this
        }

        /**
         * Writes a Type 2 [Int] to [buffer]
         * @param value [Int]
         */
        fun writeInt2(value: Int): Builder {
            writeByte((value shr 16).toByte().toInt())
            writeByte((value shr 24).toByte().toInt())
            writeByte(value.toByte().toInt())
            writeByte((value shr 8).toByte().toInt())
            return this
        }

        /**
         * Writes a little-endian [Int] to [buffer]
         * @param value [Int]
         */
        fun writeLEInt(value: Int): Builder {
            writeByte(value.toByte().toInt())
            writeByte((value shr 8).toByte().toInt())
            writeByte((value shr 16).toByte().toInt())
            writeByte((value shr 24).toByte().toInt())
            return this
        }

        /**
         * Writes a [Long] to [buffer].
         * @param value [Long]
         */
        fun writeLong(value: Long): Builder {
            buffer.writeLong(value)
            return this
        }

        /**
         * Writes a little-endian [Long] to [buffer].
         * @param value [Long]
         */
        fun writeLELong(value: Long): Builder {
            writeLEInt((value and -1L).toInt())
            writeLEInt((value shr 32).toInt())
            return this
        }

        /**
         * Writes a [String] to [buffer].
         * @param value [String]
         */
        fun writeString(value: String): Builder {
            writeBytes(value.toByteArray())
            writeByte(0)
            return this
        }

        /**
         * Enables individual encoded byte writing aka 'bit access'
         */
        fun startBitAccess(): Builder {
            bitPosition = buffer.writerIndex() * 8
            return this
        }

        /**
         * Disables 'bit access'
         */
        fun finishBitAccess(): Builder {
            buffer.writerIndex((bitPosition + 7) / 8)
            return this
        }

        /**
         * Writes a bit during 'bit access'
         * @param bitCount number of bits to be written
         * @param value bit value to be set
         */
        fun writeBits(bitCount: Int, value: Int): Builder {
            var numBits = bitCount
            var bytePos = bitPosition shr 3
            var bitOffset = 8 - (bitPosition and 7)
            bitPosition += numBits
            val pos = (bitPosition + 7) / 8
            buffer.ensureWritable(pos + 1)
            buffer.writerIndex(pos)
            var b: Byte
            while (numBits > bitOffset) {
                b = buffer.getByte(bytePos)
                buffer.setByte(bytePos, (b.toInt() and BIT_MASKS[bitOffset].inv()).toByte().toInt())
                buffer.setByte(bytePos, (b.toInt() or (value shr numBits - bitOffset and BIT_MASKS[bitOffset])).toByte().toInt())
                bytePos++
                numBits -= bitOffset
                bitOffset = 8
            }
            b = buffer.getByte(bytePos)
            if (numBits == bitOffset) {
                buffer.setByte(bytePos, (b.toInt() and BIT_MASKS[bitOffset].inv()).toByte().toInt())
                buffer.setByte(bytePos, (b.toInt() or (value and BIT_MASKS[bitOffset])).toByte().toInt())
            } else {
                buffer.setByte(bytePos, (b.toInt() and (BIT_MASKS[numBits] shl bitOffset - numBits).inv()).toByte().toInt())
                buffer.setByte(bytePos, (b.toInt() or (value and BIT_MASKS[numBits] shl bitOffset - numBits)).toByte().toInt())
            }
            return this
        }

        /**
         * Skips to position
         * @param position [Int]
         */
        fun skip(position: Int): Builder {
            for (i in 0 until position) {
                writeByte(0.toByte().toInt())
            }
            return this
        }

        /**
         * Returns current position
         * @return Current buffer position
         */
        fun position(): Int {
            return buffer.writerIndex()
        }

        /**
         * Builds a [Packet]
         * @return [Packet]
         */
        fun build(): Packet {
            return Packet(opcode, type, buffer)
        }

        companion object {
            /**
             * Bitmasks for `writeBits()`
             */
            private val BIT_MASKS = IntArray(32)

            init {
                for (i in BIT_MASKS.indices)
                    BIT_MASKS[i] = (1 shl i) - 1
            }
        }
    }

    companion object {

        private var type = Type.STANDARD

        fun GSize(): Int {
            return type.ordinal
        }

    }

}