package world.gregs.hestia.core.cache.compress

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import world.gregs.hestia.core.cache.Cache
import world.gregs.hestia.core.network.codec.packet.Packet
import world.gregs.hestia.core.network.codec.packet.PacketBuilder
import world.gregs.hestia.core.network.codec.packet.PacketWriter

internal class HuffmanTest {

    private lateinit var builder: PacketBuilder
    private lateinit var message: String

    companion object {
        @BeforeAll
        @JvmStatic
        fun start() {
            val cache = mock<Cache>()
            whenever(cache.getFile(10, 1)).thenReturn(byteArrayOf(22, 22, 22, 22, 22, 22, 21, 22, 22, 20, 22, 22, 22, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 3, 8, 22, 16, 22, 16, 17, 7, 13, 13, 13, 16, 7, 10, 6, 16, 10, 11, 12, 12, 12, 12, 13, 13, 14, 14, 11, 14, 19, 15, 17, 8, 11, 9, 10, 10, 10, 10, 11, 10, 9, 7, 12, 11, 10, 10, 9, 10, 10, 12, 10, 9, 8, 12, 12, 9, 14, 8, 12, 17, 16, 17, 22, 13, 21, 4, 7, 6, 5, 3, 6, 6, 5, 4, 10, 7, 5, 6, 4, 4, 6, 10, 5, 4, 4, 5, 7, 6, 10, 6, 10, 22, 19, 22, 14, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 21, 22, 21, 22, 22, 22, 21, 22, 22) )
            Huffman.init(cache)
        }
    }

    @BeforeEach
    fun setup() {
        builder = PacketWriter()
        message = ""
    }

    @Test
    fun `A short string`() {
        //Given
        build("Message")
        //When
        compress()
        //Then
        assertUncompressedSize(7)
        assertCompressedSize(5)
        assertDecompressed()
    }

    @Test
    fun `A long string`() {
        //Given
        build("This is a string of substantial size, perhaps enough character overlap for some decent compression")
        //When
        compress()
        //Then
        assertUncompressedSize(98)
        assertCompressedSize(54)
        assertDecompressed()
    }

    @Test
    fun `Full Alphabet`() {
        //Given
        build("abcdefghijklmnopqrstuvwxyz")
        //When
        compress()
        //Then
        assertUncompressedSize(26)
        assertCompressedSize(20)
        assertDecompressed()
    }

    @Test
    fun `Middle of a packet decompress`() {
        //Given
        build("Message")
        builder.writeByte(0)
        compress()
        builder.writeByte(0)
        //Then
        assertUncompressedSize(7)
        assertCompressedSize(7)
        assertDecompressed(1)
    }

    private fun compress() {
        Huffman.compress(message, builder)
    }

    private fun decompress(offset: Int): String? {
        val packet = builder.build()
        packet.skip(offset)
        return Huffman.decompress(packet, packet.readSmart())
    }

    private fun build(message: String) {
        this.message = message
    }

    private fun assertUncompressedSize(size: Int) {
        Assertions.assertThat(message.length).isEqualTo(size)
    }

    private fun assertCompressedSize(size: Int) {
        Assertions.assertThat(builder.build().readableBytes() - 1).isEqualTo(size)
    }

    private fun assertDecompressed(offset: Int = 0) {
        Assertions.assertThat(decompress(offset)).isEqualTo(message)
    }
}