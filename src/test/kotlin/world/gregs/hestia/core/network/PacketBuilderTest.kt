package world.gregs.hestia.core.network

import io.netty.buffer.Unpooled
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import world.gregs.hestia.core.network.packets.Packet

internal class PacketBuilderTest {

    @Test
    fun writeByte() {
        Assertions.assertThat(Packet.Builder().writeByte(0.toByte()).build().length).isEqualTo(1)
    }

    @Test
    fun writeBytes() {
        val buffer = Unpooled.buffer()
        buffer.writeByte(4)
        buffer.writeByte(2)
        val packet = Packet.Builder().writeBytes(buffer).build()
        Assertions.assertThat(packet.readByte()).isEqualTo(4)
        Assertions.assertThat(packet.readByte()).isEqualTo(2)
    }

    @Test
    fun bitAccess() {
        val builder = Packet.Builder()
        builder.startBitAccess()
        builder.writeBits(4, 2)
        builder.finishBitAccess()
        val packet = builder.build()
        Assertions.assertThat(packet.readByte()).isEqualTo(32)
    }

    @Test
    fun writeShort() {
        Assertions.assertThat(Packet.Builder().writeShort(0.toShort()).build().length).isEqualTo(2)
    }

    @Test
    fun writeBytesPosition() {
        val packet = Packet.Builder().writeBytes(byteArrayOf(3, 2, 1), 2, 1).build()
        Assertions.assertThat(packet.readByte()).isEqualTo(1)
    }

    @Test
    fun skip() {
        val packet = Packet.Builder().skip(1).writeByte(2).build()
        Assertions.assertThat(packet.readByte()).isEqualTo(0)
        Assertions.assertThat(packet.readByte()).isEqualTo(2)
    }

    @Test
    fun position() {
        val builder = Packet.Builder().skip(5)
        Assertions.assertThat(builder.position()).isEqualTo(5)
    }

    @Test
    fun build() {
        val builder = Packet.Builder(1, Packet.Type.VAR_SHORT).writeShort(123)
        val packet = builder.build()
        Assertions.assertThat(packet).isInstanceOf(Packet::class.java)
        Assertions.assertThat(packet.opcode).isEqualTo(1)
        Assertions.assertThat(packet.type).isEqualTo(Packet.Type.VAR_SHORT)
        Assertions.assertThat(packet.buffer.array()).containsExactly(0, 123)
    }
}