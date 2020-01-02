package world.gregs.hestia.core.network.codec.packet

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import world.gregs.hestia.core.cache.crypto.Cipher
import world.gregs.hestia.core.services.int

open class PacketWriter(private var opcode: Int? = null, private var type: Packet.Type = Packet.Type.STANDARD, final override val buffer: ByteBuf = Unpooled.buffer(), private val cipher: Cipher? = null) : PacketBuilder {
    private var sizeIndex = 0
    private var bitIndex = 0
    private var mode = AccessMode.BYTE

    init {
        if (opcode != null) {
            writeOpcode(opcode!!, type)
        }
    }

    final override fun writeOpcode(opcode: Int, type: Packet.Type) {
        this.type = type
        this.opcode = opcode
        if (cipher != null) {
            if (opcode >= 128) {
                writeByte(((opcode shr 8) + 128) + cipher.nextInt())
                writeByte(opcode + cipher.nextInt())
            } else {
                writeByte(opcode + cipher.nextInt())
            }
        } else {
            writeSmart(opcode)
        }
        //Write opcode
        //Save index where size is written
        sizeIndex = buffer.writerIndex()
        //Write length placeholder
        when (type) {
            Packet.Type.VAR_BYTE -> writeByte(0)
            Packet.Type.VAR_SHORT -> writeShort(0)
            else -> {
            }
        }
    }

    final override fun writeSize() {
        if (sizeIndex > 0) {
            val index = buffer.writerIndex()
            //The length of the headless packet
            val size = index - sizeIndex
            //Reset to the header size placeholder
            buffer.writerIndex(sizeIndex)
            //Write the packet length (accounting for placeholder)
            when (type) {
                Packet.Type.VAR_BYTE -> writeByte(size - 1)
                Packet.Type.VAR_SHORT -> writeShort(size - 2)
                else -> {
                }
            }
            //Reset index to end
            buffer.writerIndex(index)
        }
    }

    final override fun writeSmart(value: Int): PacketBuilder {
        if (value >= 128) {
            writeShort(value + 32768)
        } else {
            writeByte(value)
        }
        return this
    }

    final override fun writeString(value: String?): PacketBuilder {
        if(value != null) {
            writeBytes(value.toByteArray())
        }
        writeByte(0)
        return this
    }

    final override fun writeBytes(value: ByteArray): PacketBuilder {
        buffer.writeBytes(value)
        return this
    }

    final override fun writeBytes(value: ByteBuf): PacketBuilder {
        buffer.writeBytes(value)
        return this
    }

    final override fun writeBytes(data: ByteArray, offset: Int, length: Int): PacketBuilder {
        buffer.writeBytes(data, offset, length)
        return this
    }

    final override fun writeBytes(data: ByteBuf, offset: Int, length: Int): PacketBuilder {
        buffer.writeBytes(data, offset, length)
        return this
    }

    final override fun startBitAccess(): PacketBuilder {
        /*if (mode == AccessMode.BIT) {
            throw IllegalStateException("Already in bit access mode")
        }*/
        mode = AccessMode.BIT
        bitIndex = buffer.writerIndex() * 8
        return this
    }

    final override fun finishBitAccess(): PacketBuilder {
        /*if (mode == AccessMode.BYTE) {
            throw IllegalStateException("Already in byte access mode")
        }*/
        mode = AccessMode.BYTE
        buffer.writerIndex((bitIndex + 7) / 8)
        return this
    }

    final override fun writeBits(bitCount: Int, value: Boolean): PacketBuilder {
        return writeBits(bitCount, value.int)
    }

    final override fun writeBits(bitCount: Int, value: Int): PacketBuilder {
//        checkBitAccess()
        var numBits = bitCount

        var bytePos = bitIndex shr 3
        var bitOffset = 8 - (bitIndex and 7)
        bitIndex += numBits

        var requiredSpace = bytePos - buffer.writerIndex() + 1
        requiredSpace += (numBits + 7) / 8
        buffer.ensureWritable(requiredSpace)

        while (numBits > bitOffset) {
            var tmp = buffer.getByte(bytePos).toInt()
            tmp = tmp and BIT_MASKS[bitOffset].inv()
            tmp = tmp or (value shr numBits - bitOffset and BIT_MASKS[bitOffset])
            buffer.setByte(bytePos++, tmp)
            numBits -= bitOffset
            bitOffset = 8
        }
        if (numBits == bitOffset) {
            var tmp = buffer.getByte(bytePos).toInt()
            tmp = tmp and BIT_MASKS[bitOffset].inv()
            tmp = tmp or (value and BIT_MASKS[bitOffset])
            buffer.setByte(bytePos, tmp)
        } else {
            var tmp = buffer.getByte(bytePos).toInt()
            tmp = tmp and (BIT_MASKS[numBits] shl bitOffset - numBits).inv()
            tmp = tmp or (value and BIT_MASKS[numBits] shl bitOffset - numBits)
            buffer.setByte(bytePos, tmp)
        }
        return this
    }

    final override fun skip(position: Int): PacketBuilder {
        for (i in 0 until position) {
            writeByte(0)
        }
        return this
    }

    final override fun position(): Int {
        return buffer.writerIndex()
    }

    final override fun build(): Packet {
        writeSize()
        return PacketReader(opcode ?: -1, type, buffer)
    }

    override fun write(type: DataType, value: Number, modifier: Modifier, order: Endian) {
//        checkByteAccess()
        val longValue = value.toLong()
        when (order) {
            Endian.BIG, Endian.LITTLE -> {
                val range = if (order == Endian.LITTLE) 0 until type.length else type.length - 1 downTo 0
                for (i in range) {
                    if (i == 0 && modifier != Modifier.NONE) {
                        when (modifier) {
                            Modifier.ADD -> buffer.writeByte((longValue + 128).toByte().toInt())
                            Modifier.INVERSE -> buffer.writeByte((-longValue).toByte().toInt())
                            Modifier.SUBTRACT -> buffer.writeByte((128 - longValue).toByte().toInt())
                            else -> throw IllegalArgumentException("Unknown byte modifier")
                        }
                    } else {
                        buffer.writeByte((longValue shr i * 8).toByte().toInt())
                    }
                }
            }
            Endian.MIDDLE -> {
                if (modifier != Modifier.NONE && modifier != Modifier.INVERSE) {
                    throw IllegalArgumentException("Middle endian doesn't support variable modifier $modifier")
                }

                if (type != DataType.INT) {
                    throw IllegalArgumentException("Middle endian can only be used with an integer")
                }

                val range = listOf(8, 0, 24, 16)
                //Reverse range if inverse modifier
                for (i in if (modifier == Modifier.NONE) range else range.reversed()) {
                    buffer.writeByte((longValue shr i).toByte().toInt())
                }
            }
        }
    }

    /**
     * The packet write mode
     */
    private enum class AccessMode {
        BYTE,
        BIT;
    }

    /**
     * Checks the write mode is byte
     */
    private fun checkByteAccess() {
        if (mode != AccessMode.BYTE) {
            throw IllegalStateException("Can't write bytes while in bit access mode")
        }
    }

    /**
     * Checks the write mode is bit
     */
    private fun checkBitAccess() {
        if (mode != AccessMode.BIT) {
            throw IllegalStateException("Can't write bits while in byte access mode")
        }
    }

    companion object {
        /**
         * Bit masks for [writeBits]
         */
        private val BIT_MASKS = IntArray(32)

        init {
            for (i in BIT_MASKS.indices)
                BIT_MASKS[i] = (1 shl i) - 1
        }
    }
}