package world.gregs.hestia.core.network.protocol.packet

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import world.gregs.hestia.core.network.codec.packet.*

internal class PacketWriterTest {

    private lateinit var builder: PacketBuilder
    private lateinit var packet: Packet

    @Test
    fun `Write static sized header`() {
        //Given
        builder(3)
        byte(2)
        //When
        build()
        //Then
        assertOpcode(3)
        assertByte(2)
    }

    @Test
    fun `Write byte sized header`() {
        //Given
        builder(3, Packet.Type.VAR_BYTE)
        byte(2)
        //When
        build()
        //Then
        assertOpcode(3)
        assertSize(1, Packet.Type.VAR_BYTE)
        assertByte(2)
    }

    @Test
    fun `Write short sized header`() {
        //Given
        builder(3, Packet.Type.VAR_SHORT)
        byte(2)
        //When
        build()
        //Then
        assertOpcode(3)
        assertSize(1, Packet.Type.VAR_SHORT)
        assertByte(2)

    }

    @Test
    fun `Write bit access`() {
        //Given
        builder(3, Packet.Type.VAR_BYTE)
        //When
        start()
        bits(4, 2)
        finish()
        build()
        //Then
        assertOpcode(3)
        assertSize(1, Packet.Type.VAR_BYTE)
        assertByte(32)
    }

    @Test
    fun skip() {
        //Given
        builder(2)
        //When
        skip(4)
        byte(1)
        build()
        //Then
        assertOpcode(2)
        for(i in 0 until 4) {
            assertByte(0)
        }
        assertByte(1)
    }

    @Test
    fun `Write bytes position`() {
        //Given
        builder()
        //When
        bytes(byteArrayOf(3, 2, 1), 2, 1)
        build()
        //Then
        assertByte(1)
    }

    @Test
    fun `Correct position`() {
        //Given
        builder()
        //When
        skip(5)
        //Then
        assertThat(builder.position()).isEqualTo(5)
    }

    @Test
    fun `Builds correctly`() {
        //Given
        builder(1, Packet.Type.VAR_SHORT)
        short(123)
        //When
        build()
        //Then
        assertThat(packet).isInstanceOf(Packet::class.java)
        assertThat(packet.opcode).isEqualTo(1)
        assertThat(packet.type).isEqualTo(Packet.Type.VAR_SHORT)
        assertThat(packet.buffer.array()).startsWith(1, 0, 2, 0, 123)
    }

    @Test
    fun `Write byte`() {
        //Given
        builder()
        //When
        byte(2)
        byte(-2)
        build()
        //Then
        assertBytes(2, -2)
    }

    @Test
    fun `Write byte add`() {
        //Given
        builder()
        //When
        byte(2, Modifier.ADD)
        byte(-2, Modifier.ADD)
        build()
        //Then
        assertBytes(-126, 126)
    }

    @Test
    fun `Write byte inverse`() {
        //Given
        builder()
        //When
        byte(2, Modifier.INVERSE)
        byte(-2, Modifier.INVERSE)
        build()
        //Then
        assertBytes(-2, 2)
    }

    @Test
    fun `Write byte subtract`() {
        //Given
        builder()
        //When
        byte(2, Modifier.SUBTRACT)
        byte(-2, Modifier.SUBTRACT)
        build()
        //Then
        assertBytes(126, -126)
    }

    @Test
    fun `Write short`() {
        //Given
        builder()
        //When
        short(2)
        short(-2)
        build()
        //Then
        assertBytes(0, 2, -1, -2)
    }

    @Test
    fun `Write short add`() {
        //Given
        builder()
        //When
        short(2, Modifier.ADD)
        short(-2, Modifier.ADD)
        build()
        //Then
        assertBytes(0, -126, -1, 126)
    }

    @Test
    fun `Write short little endian`() {
        //Given
        builder()
        //When
        short(2, endian = Endian.LITTLE)
        short(-2, endian = Endian.LITTLE)
        build()
        //Then
        assertBytes(2, 0, -2, -1)
    }

    @Test
    fun `Write short little endian add`() {
        //Given
        builder()
        //When
        short(2, Modifier.ADD, Endian.LITTLE)
        short(-2, Modifier.ADD, Endian.LITTLE)
        build()
        //Then
        assertBytes(-126, 0, 126, -1)
    }

    @Test
    fun `Write int`() {
        //Given
        builder()
        //When
        int(2)
        int(-2)
        build()
        //Then
        assertBytes(0, 0, 0, 2, -1, -1, -1, -2)
    }

    @Test
    fun `Write int middle endian`() {
        //Given
        builder()
        //When
        int(2, endian = Endian.MIDDLE)
        int(-2, endian = Endian.MIDDLE)
        build()
        //Then
        assertBytes(0, 2, 0, 0, -1, -2, -1, -1)
    }

    @Test
    fun `Write int middle endian inverse`() {
        //Given
        builder()
        //When
        int(2, Modifier.INVERSE, Endian.MIDDLE)
        int(-2, Modifier.INVERSE, Endian.MIDDLE)
        build()
        //Then
        assertBytes(0, 0, 2, 0, -1, -1, -2, -1)
    }

    @Test
    fun `Write int little endian`() {
        //Given
        builder()
        //When
        int(2, endian = Endian.LITTLE)
        int(-2, endian = Endian.LITTLE)
        build()
        //Then
        assertBytes(2, 0, 0, 0, -2, -1, -1, -1)
    }

    @Test
    fun `Write int little endian inverse`() {
        //Given
        builder()
        //When
        builder.writeInt(2, Modifier.INVERSE, Endian.LITTLE)
        builder.writeInt(-2, Modifier.INVERSE, Endian.LITTLE)
        build()
        //Then
        assertBytes(-2, 0, 0, 0, 2, -1, -1, -1)
    }

    @Test
    fun `Write string`() {
        //Given
        builder()
        //When
        builder.writeString("1")
        build()
        //Then
        assertBytes(49, 0)
    }

    @Test
    fun `Write long`() {
        //Given
        builder()
        //When
        builder.writeLong(2)
        builder.writeLong(-2)
        build()
        //Then
        assertBytes(0, 0, 0, 0, 0, 0, 0, 2, -1, -1, -1, -1, -1, -1, -1 , -2)
    }

    @Test
    fun `Write medium`() {
        //Given
        builder()
        //When
        builder.writeMedium(2)
        builder.writeMedium(-2)
        build()
        //Then
        assertBytes(0, 0, 2, -1, -1, -2)
    }

    @Test
    fun `Write smart`() {
        //Given
        builder()
        //When
        builder.writeSmart(1)
        builder.writeSmart(500)
        build()
        //Then
        assertBytes(1, -127, -12)
    }

    private fun builder(opcode: Int? = null, type: Packet.Type = Packet.Type.STANDARD) {
        builder = PacketWriter(opcode, type)
    }

    private fun build() {
        packet = builder.build()
    }

    private fun start() {
        builder.startBitAccess()
    }

    private fun finish() {
        builder.finishBitAccess()
    }

    private fun skip(count: Int) {
        builder.skip(count)
    }

    private fun bits(count: Int, value: Int) {
        builder.writeBits(count, value)
    }

    private fun bytes(array: ByteArray, offset: Int, length: Int) {
        builder.writeBytes(array, offset, length)
    }

    private fun byte(value: Int, type: Modifier = Modifier.NONE) {
        builder.writeByte(value, type)
    }

    private fun byte(value: Int) {
        builder.writeByte(value)
    }

    private fun short(value: Int, type: Modifier = Modifier.NONE, endian: Endian = Endian.BIG) {
        builder.writeShort(value, type, endian)
    }

    private fun int(value: Int, type: Modifier = Modifier.NONE, endian: Endian = Endian.BIG) {
        builder.writeInt(value, type, endian)
    }

    private fun short(value: Int) {
        builder.writeShort(value)
    }

    private fun assertOpcode(opcode: Int) {
        assertThat(packet.readSmart()).isEqualTo(opcode)
    }

    private fun assertByte(value: Int) {
        assertThat(packet.readByte()).isEqualTo(value)
    }

    private fun assertSize(size: Int, type: Packet.Type) {
        assertThat(if(type == Packet.Type.VAR_BYTE) packet.readByte() else packet.readShort()).isEqualTo(size)
    }

    private fun assertBytes(vararg bytes: Int) {
        assertThat(packet.buffer.array()).startsWith(*bytes)
    }
}