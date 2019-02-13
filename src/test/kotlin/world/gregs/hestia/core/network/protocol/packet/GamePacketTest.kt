package world.gregs.hestia.core.network.protocol.packet

import io.netty.buffer.Unpooled
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import world.gregs.hestia.core.network.codec.packet.Endian
import world.gregs.hestia.core.network.codec.packet.PacketReader
import world.gregs.hestia.core.network.codec.packet.Packet
import world.gregs.hestia.core.network.codec.packet.Modifier

internal class GamePacketTest {

    private lateinit var packet: Packet

    private fun packet(vararg bytes: Int) {
        val buffer = Unpooled.buffer(bytes.size)
        bytes.forEach { buffer.writeByte(it) }
        packet = PacketReader(buffer = buffer)
    }

    @Test
    fun `Read byte`() {
        //Given
        packet(2, -2)
        //Then
        assertByte(2)
        assertByte(-2)
    }

    @Test
    fun `Read byte add`() {
        //Given
        packet(-126, 126)
        //Then
        assertByte(2, Modifier.ADD)
        assertByte(-2, Modifier.ADD)
    }

    @Test
    fun `Read byte inverse`() {
        //Given
        packet(-2, 2)
        //Then
        assertByte(2, Modifier.INVERSE)
        assertByte(-2, Modifier.INVERSE)
    }

    @Test
    fun `Read byte subtract`() {
        //Given
        packet(126, -126)
        //Then
        assertByte(2, Modifier.SUBTRACT)
        assertByte(-2, Modifier.SUBTRACT)
    }

    @Test
    fun `Read short`() {
        //Given
        packet(0, 2, -1, -2)
        //Then
        assertShort(2)
        assertShort(-2)
    }

    @Test
    fun `Read short add`() {
        //Given
        packet(0, -126, -1, 126)
        //Then
        assertShort(2, Modifier.ADD)
        assertShort(-2, Modifier.ADD)
    }

    @Test
    fun `Read short little endian`() {
        //Given
        packet(2, 0, -2, -1)
        //Then
        assertShort(2, endian = Endian.LITTLE)
        assertShort(-2, endian = Endian.LITTLE)
    }

    @Test
    fun `Read short little endian add`() {
        //Given
        packet(-126, 0, 126, -1)
        //Then
        assertShort(2, Modifier.ADD, Endian.LITTLE)
        assertShort(-2, Modifier.ADD, Endian.LITTLE)
    }

    @Test
    fun `Read int`() {
        //Given
        packet(0, 0, 0, 2, -1, -1, -1, -2)
        //Then
        assertInt(2)
        assertInt(-2)
    }

    @Test
    fun `Read int middle endian inverse`() {
        //Given
        packet(0, 0, 2, 0, -1, -1, -2, -1)
        //Then
        assertInt(2, Modifier.INVERSE, Endian.MIDDLE)
        assertInt(-2, Modifier.INVERSE, Endian.MIDDLE)
    }

    @Test
    fun `Read int little endian`() {
        //Given
        packet(2, 0, 0, 0, -2, -1, -1, -1)
        //Then
        assertInt(2, endian = Endian.LITTLE)
        assertInt(-2, endian = Endian.LITTLE)
    }

    @Test
    fun `Read int inverse middle endian`() {
        //Given
        packet(0, 2, 0, 0, -1, -2, -1, -1)
        //Then
        assertInt(2, endian = Endian.MIDDLE)
        assertInt(-2, endian = Endian.MIDDLE)
    }

    @Test
    fun `Read string`() {
        //Given
        packet(49, 0)
        //Then
        assertThat(packet.readString()).isEqualTo("1")
    }

    @Test
    fun `Read medium`() {
        //Given
        packet(0, 0, 2, -1, -1, -2)
        //Then
        assertThat(packet.readMedium()).isEqualTo(2)
        assertThat(packet.readMedium()).isEqualTo(-2)
    }

    @Test
    fun `Read smart`() {
        //Given
        packet(1, -127, -12)
        //Then
        assertThat(packet.readSmart()).isEqualTo(1)
        assertThat(packet.readSmart()).isEqualTo(500)
    }

    @Test
    fun `Read long`() {
        //Given
        packet(0, 0, 0, 0, 0, 0, 0, 2, -1, -1, -1, -1, -1, -1, -1, -2)
        //Then
        assertThat(packet.readLong()).isEqualTo(2)
        assertThat(packet.readLong()).isEqualTo(-2)
    }

    private fun assertByte(value: Int, type: Modifier = Modifier.NONE) {
        assertThat(packet.readByte(type)).isEqualTo(value)
    }

    private fun assertShort(value: Int, type: Modifier = Modifier.NONE, endian: Endian = Endian.BIG) {
        assertThat(packet.readShort(type, endian)).isEqualTo(value)
    }

    private fun assertInt(value: Int, type: Modifier = Modifier.NONE, endian: Endian = Endian.BIG) {
        assertThat(packet.readInt(type, endian)).isEqualTo(value)
    }
}