package world.gregs.hestia.core.cache.crypto

import java.util.zip.CRC32

object CRC {
    private val CRC_32 = CRC32()

    @JvmOverloads
    fun getHash(data: ByteArray, offset: Int = 0, length: Int = data.size): Int {
        synchronized(CRC_32) {
            CRC_32.update(data, offset, length)
            val hash = CRC_32.value.toInt()
            CRC_32.reset()
            return hash
        }
    }
}
