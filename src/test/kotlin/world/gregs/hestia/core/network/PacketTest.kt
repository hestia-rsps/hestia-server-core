package world.gregs.hestia.core.network

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import world.gregs.hestia.core.network.packets.Packet

internal class PacketTest {

    private lateinit var packet: Packet

    @BeforeEach
    internal fun setUp() {
        packet = getValidPacket()
    }

    @Test
    fun getLength() {
        assertThat(Packet.Builder().writeByte(0).build().length).isEqualTo(1)
        assertThat(Packet.Builder().writeShort(0).build().length).isEqualTo(2)
        assertThat(Packet.Builder().writeInt(0).build().length).isEqualTo(4)
        assertThat(Packet.Builder().writeString("").build().length).isEqualTo(1)
        assertThat(Packet.Builder().writeLong(0).build().length).isEqualTo(8)
        assertThat(packet.length).isEqualTo(18)
    }

    @Test
    fun isRaw() {
        assertThat(packet.hasOpcode).isTrue()
        assertThat(Packet.Builder().build().hasOpcode).isFalse()
    }

    @Test
    fun read() {
        val emptyArray = ByteArray(packet.length)
        packet.readBytes(emptyArray)
        assertThat(emptyArray.size).isEqualTo(packet.length)
        assertThat(emptyArray).isEqualTo(packet.buffer.array())
    }

    @Test
    fun readByte() {
        assertThat(packet.readByte()).isEqualTo(14)
        assertThat(Packet.Builder().writeByte(-1).build().readByte()).isEqualTo(-1)
    }

    @Test
    fun readUnsignedByte() {
        assertThat(packet.readUnsignedByte()).isEqualTo(14)
        assertThat(Packet.Builder().writeByte(-1).build().readUnsignedByte()).isNotEqualTo(-1)
    }

    @Test
    fun readShort() {
        packet.readByte()
        assertThat(packet.readShort()).isEqualTo(59)
        assertThat(Packet.Builder().writeShort(-1).build().readShort()).isEqualTo(-1)
    }

    @Test
    fun readUnsignedShort() {
        packet.readByte()
        assertThat(packet.readUnsignedShort()).isEqualTo(59)
        assertThat(Packet.Builder().writeShort(-1).build().readUnsignedByte()).isNotEqualTo(-1)
    }

    @Test
    fun readInt() {
        packet.readByte()
        packet.readShort()
        assertThat(packet.readInt()).isEqualTo(2)
        assertThat(Packet.Builder().writeInt(-1).build().readInt()).isEqualTo(-1)
    }

    @Test
    fun readLong() {
        packet.readByte()
        packet.readShort()
        packet.readInt()
        assertThat(packet.readLong()).isEqualTo(6)
        assertThat(Packet.Builder().writeLong(-1).build().readLong()).isEqualTo(-1)
    }

    @Test
    fun readByteA() {
        assertThat(Packet.Builder().writeByteA(1).build().readByteA()).isEqualTo(1)
        assertThat(Packet.Builder().writeByteA(-1).build().readByteA()).isEqualTo(-1)
    }

    @Test
    fun readByteC() {
        assertThat(Packet.Builder().writeByteC(1).build().readByteC()).isEqualTo(1)
        assertThat(Packet.Builder().writeByteC(-1).build().readByteC()).isEqualTo(-1)
    }

    @Test
    fun readByteS() {
        assertThat(Packet.Builder().writeByteS(1).build().readByteS()).isEqualTo(1)
        assertThat(Packet.Builder().writeByteS(-1).build().readByteS()).isEqualTo(-1)
    }

    @Test
    fun readLEShortA() {
        assertThat(Packet.Builder().writeLEShortA(1).build().readLEShortA()).isEqualTo(1)
    }

    @Test
    fun readLEShort() {
        assertThat(Packet.Builder().writeLEShort(1).build().readLEShort()).isEqualTo(1)
    }

    @Test
    fun readTriByte() {
        assertThat(Packet.Builder().writeMedium(1).build().readTriByte()).isEqualTo(1)
    }

    @Test
    fun readShortA() {
        assertThat(Packet.Builder().writeShortA(1).build().readShortA()).isEqualTo(1)
    }

    @Test
    fun readLEInt() {
        assertThat(Packet.Builder().writeLEInt(1).build().readLEInt()).isEqualTo(1)
        assertThat(Packet.Builder().writeLEInt(-1).build().readLEInt()).isEqualTo(-1)
    }

    @Test
    fun readString() {
        packet.readByte()
        packet.readShort()
        packet.readInt()
        packet.readLong()
        assertThat(packet.readString()).isEqualTo("53")
        assertThat(Packet.Builder().writeString("1").build().readString()).isEqualTo("1")
        assertThat(Packet.Builder().writeString("").build().readString()).isEqualTo("")
    }

    @Test
    fun readSmart() {
        assertThat(Packet.Builder().writeSmart(0).build().readSmart()).isEqualTo(0)
        assertThat(Packet.Builder().writeSmart(130).build().readSmart()).isEqualTo(128)
        assertThat(Packet.Builder().writeSmart(-130).build().readSmart()).isEqualTo(126)
    }

    @Test
    fun remaining() {
        packet.readByte()
        assertThat(packet.readableBytes()).isEqualTo(17)
        packet.readShort()
        assertThat(packet.readableBytes()).isEqualTo(15)
        packet.readInt()
        assertThat(packet.readableBytes()).isEqualTo(11)
        packet.readLong()
        assertThat(packet.readableBytes()).isEqualTo(3)
        packet.readString()
        assertThat(packet.readableBytes()).isEqualTo(0)
    }

    @Test
    fun getOpcode() {
        assertThat(packet.opcode).isEqualTo(3)
    }

    @Test
    fun getType() {
        assertThat(packet.type).isEqualTo(Packet.Type.VAR_BYTE)
    }

    @Test
    fun getBuffer() {
        assertThat(packet.buffer.array().size).isNotEqualTo(0)
    }

    private fun getValidPacket(): Packet {
        return Packet.Builder(3, Packet.Type.VAR_BYTE).writeByte(14).writeShort(59).writeInt(2).writeLong(6).writeString("53").build()
    }
}