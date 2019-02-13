package world.gregs.hestia.core.network.codec.packet

import io.netty.buffer.ByteBuf
import world.gregs.hestia.core.services.int

/**
 * Used to instantiate a [Packet]
 */
interface PacketBuilder {

    val buffer: ByteBuf

    /**
     * Writes packet header; operation id and place holder for packet size
     * @param opcode The packets id
     * @param type The packets size type
     */
    fun writeOpcode(opcode: Int, type: Packet.Type = Packet.Type.STANDARD)

    /**
     * Writes the packets size to the header
     */
    fun writeSize()

    /**
     * Writes a byte to [buffer].
     * @param value [Int]
     */
    fun writeByte(value: Int, type: Modifier = Modifier.NONE): PacketBuilder {
        write(DataType.BYTE, value, type)
        return this
    }


    /**
     * Writes a Smart to [buffer]
     * @param value [Int]
     */
    fun writeSmart(value: Int): PacketBuilder

    /**
     * Writes a [Short] to [buffer].
     * @param value [Int]
     */
    fun writeShort(value: Int, type: Modifier = Modifier.NONE, order: Endian = Endian.BIG): PacketBuilder {
        write(DataType.SHORT, value, type, order)
        return this
    }

    /**
     * Writes a Medium [Int] to [buffer]
     * @param value [Int]
     */
    fun writeMedium(value: Int): PacketBuilder {
        write(DataType.MEDIUM, value)
        return this
    }

    /**
     * Writes a [Int] to [buffer].
     * @param value [Int]
     */
    fun writeInt(value: Int, type: Modifier = Modifier.NONE, order: Endian = Endian.BIG): PacketBuilder {
        write(DataType.INT, value, type, order)
        return this
    }

    /**
     * Writes a [Long] to [buffer].
     * @param value [Long]
     */
    fun writeLong(value: Long): PacketBuilder {
        write(DataType.LONG, value)
        return this
    }

    /**
     * Writes a [String] to [buffer].
     * @param value [String]
     */
    fun writeString(value: String): PacketBuilder

    /**
     * Writes a boolean as a byte to [buffer].
     * @param value [Boolean]
     */
    fun writeByte(value: Boolean, type: Modifier = Modifier.NONE): PacketBuilder {
        return writeByte(value.int, type)
    }

    /**
     * Writes a [ByteArray] to [buffer].
     * @param value [ByteArray]
     */
    fun writeBytes(value: ByteArray): PacketBuilder

    /**
     * Writes a [ByteBuf] to [buffer].
     * @param value [ByteBuf]
     */
    fun writeBytes(value: ByteBuf): PacketBuilder

    /**
     * Writes part of a [ByteArray] to [buffer] at position
     * @param data [ByteArray] to be read from
     * @param offset Index to start writing to
     * @param length Amount of array to be written
     */
    fun writeBytes(data: ByteArray, offset: Int, length: Int): PacketBuilder

    /**
     * Writes part of a [ByteBuf] to [buffer] at position
     * @param data [ByteBuf] to be read from
     * @param offset Index to start writing to
     * @param length Amount of array to be written
     */
    fun writeBytes(data: ByteBuf, offset: Int, length: Int): PacketBuilder

    /**
     * Enables individual encoded byte writing aka 'bit access'
     */
    fun startBitAccess(): PacketBuilder

    /**
     * Disables 'bit access'
     */
    fun finishBitAccess(): PacketBuilder

    /**
     * Writes a bit during 'bit access'
     * @param bitCount number of bits to be written
     * @param value bit value to be set
     */
    fun writeBits(bitCount: Int, value: Int): PacketBuilder

    /**
     * Writes [value] to the buffer encoded with [type], [modifier] and [order]
     * @param value The value to write
     * @param type The byte type to read
     * @param modifier The first byte read modifier
     * @param order The endianness
     * @return The read value
     */
    fun write(type: DataType, value: Number, modifier: Modifier = Modifier.NONE, order: Endian = Endian.BIG)

    /**
     * Skips to position
     * @param position [Int]
     */
    fun skip(position: Int): PacketBuilder

    /**
     * Returns current position
     * @return Current buffer position
     */
    fun position(): Int


    /**
     * Builds a [Packet]
     * @return [Packet]
     */
    fun build(): Packet
}
