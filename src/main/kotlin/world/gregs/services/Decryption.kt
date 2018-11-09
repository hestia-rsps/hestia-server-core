package world.gregs.services

import world.gregs.network.packets.Packet
import java.math.BigInteger

object Decryption {
    /**
     * Decrypt rsa bytes received from client
     */
    fun decryptRsa(data: ByteArray, exponent: BigInteger, modulus: BigInteger): ByteArray {
        return BigInteger(data).modPow(exponent, modulus).toByteArray()
    }

    fun decodeXTEA(packet: Packet, keys: IntArray) {
        packet.apply {
            val start = buffer.readerIndex()
            val end = length
            val write = buffer.readerIndex()
            buffer.readerIndex(start)
            buffer.writerIndex(start)
            val loops = (end - start) / 8
            val delta = -0x61c88647
            for (loop in 0 until loops) {
                buffer.readerIndex(buffer.writerIndex())
                buffer.writerIndex(buffer.writerIndex() + 8)
                var first = readInt()
                var second = readInt()
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
    }
}