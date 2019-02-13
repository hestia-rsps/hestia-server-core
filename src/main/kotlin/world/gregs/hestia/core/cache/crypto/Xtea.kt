package world.gregs.hestia.core.cache.crypto

import io.netty.buffer.ByteBuf
import java.nio.ByteBuffer

object Xtea {

    /**
     * The golden ratio.
     */
    private const val GOLDEN_RATIO = -0x61c88647

    /**
     * The number of rounds.
     */
    private const val ROUNDS = 32

    /**
     * Deciphers the specified [ByteBuffer] with the given key.
     * @param buffer The buffer.
     * @param key The key.
     * @throws IllegalArgumentException if the key is not exactly 4 elements
     * long.
     */
    fun decipher(buffer: ByteBuffer, key: IntArray) {
        if (key.size != 4) {
            throw IllegalArgumentException()
        }

        var i = 0
        while (i < buffer.limit()) {
            var sum = GOLDEN_RATIO * ROUNDS
            var v0 = buffer.getInt(i * 4)
            var v1 = buffer.getInt(i * 4 + 4)
            for (j in 0 until ROUNDS) {
                v1 = (v0 shl 4 xor (v0 shr 5)) + v0 xor sum + key[sum shr 11 and 3]
                sum -= GOLDEN_RATIO
                v0 = (v1 shl 4 xor (v1 shr 5)) + v1 xor sum + key[sum and 3]
            }
            buffer.putInt(i * 4, v0)
            buffer.putInt(i * 4 + 4, v1)
            i += 8
        }
    }


    /**
     * Deciphers the specified [ByteBuf] with the given keys.
     * @param buffer The buffer.
     * @param keys The keys
     */
    fun decipher(buffer: ByteBuf, keys: IntArray) {
        val start = buffer.readerIndex()
        val end = buffer.readableBytes()
        val write = buffer.readerIndex()
        buffer.readerIndex(start)
        buffer.writerIndex(start)
        val loops = (end - start) / 8
        val delta = -0x61c88647
        for (loop in 0 until loops) {
            buffer.readerIndex(buffer.writerIndex())
            buffer.writerIndex(buffer.writerIndex() + 8)
            var first = buffer.readInt()
            var second = buffer.readInt()
            var sum = -0x3910c8e0
            var reverse = 32
            while (reverse-- > 0) {
                second -= keys[(sum and 0x1c84).ushr(11)] + sum xor (first.ushr(5) xor (first shl 4)) + first
                sum -= delta
                first -= (second.ushr(5) xor (second shl 4)) + second xor keys[sum and 3] + sum
            }

            buffer.readerIndex(buffer.readerIndex() - 8)
            buffer.writerIndex(buffer.writerIndex() - 8)

            buffer.writeInt(first)
            buffer.writeInt(second)
        }
        buffer.readerIndex(write)
    }

    /**
     * Enciphers the specified [ByteBuffer] with the given key.
     * @param buffer The buffer.
     * @param key The key.
     * @throws IllegalArgumentException if the key is not exactly 4 elements
     * long.
     */
    fun encipher(buffer: ByteBuffer, key: IntArray) {
        if (key.size != 4)
            throw IllegalArgumentException()

        var i = 0
        while (i < buffer.limit()) {
            var sum = 0
            var v0 = buffer.getInt(i * 4)
            var v1 = buffer.getInt(i * 4 + 4)
            for (j in 0 until ROUNDS) {
                v0 = (v1 shl 4 xor (v1 shr 5)) + v1 xor sum + key[sum and 3]
                sum += GOLDEN_RATIO
                v1 = (v0 shl 4 xor (v0 shr 5)) + v0 xor sum + key[sum shr 11 and 3]
            }
            buffer.putInt(i * 4, v0)
            buffer.putInt(i * 4 + 4, v1)
            i += 8
        }
    }
}
