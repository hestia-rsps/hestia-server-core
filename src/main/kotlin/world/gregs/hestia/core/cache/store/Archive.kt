package world.gregs.hestia.core.cache.store

import world.gregs.hestia.core.cache.compress.BZip2
import world.gregs.hestia.core.cache.compress.GZip
import world.gregs.hestia.core.cache.crypto.Xtea
import java.nio.Buffer
import java.nio.ByteBuffer

class Archive(val id: Int, archive: ByteArray, val keys: IntArray?) {
    var revision: Int = 0
        private set
    var compression: Int = 0
        private set
    var data: ByteArray? = null
        private set

    val decompressedLength: Int
        get() = data!!.size

    init {
        decompress(archive)
    }

    private fun decompress(archive: ByteArray) {
        val buffer = ByteBuffer.wrap(archive)
        if (keys != null && keys.isNotEmpty()) {
            Xtea.decipher(buffer, keys)
        }

        compression = buffer.getUByte()
        val compressedLength = buffer.int
        if (compressedLength in 0..MAX_VALID_ARCHIVE_LENGTH) {
            val length: Int
            when (compression) {
                NO_COMPRESSION -> {
                    data = ByteArray(compressedLength)
                    checkRevision(buffer, compressedLength)
                    buffer.get(data!!, 0, compressedLength)
                }
                BZIP2_COMPRESSION -> {
                    length = buffer.int
                    if (length <= 0) {
                        data = null
                    } else {
                        data = ByteArray(length)
                        checkRevision(buffer, compressedLength)
                        BZip2.decompress(data!!, archive, 9)
                    }
                }
                else -> {
                    length = buffer.int
                    if (length in 1..1000000000) {
                        data = ByteArray(length)
                        checkRevision(buffer, compressedLength)
                        GZip.decompress(buffer, data!!)
                    } else {
                        data = null
                    }
                }
            }
        } else {
            throw RuntimeException("INVALID ARCHIVE HEADER")
        }
    }

    /**
     * Converted to [Buffer] to ensure Java 8 (Kotlin) compatibility with Java 11
     */
    private fun checkRevision(buffer: Buffer, compressedLength: Int) {
        val offset = buffer.position()
        if (buffer.limit() - (compressedLength + offset) >= 2) {
            buffer.position(buffer.limit() - 2)
            revision = (buffer as ByteBuffer).getUShort()
            (buffer as Buffer).position(offset)
        } else {
            revision = -1
        }
    }

    companion object {
        private const val NO_COMPRESSION = 0
        private const val BZIP2_COMPRESSION = 1
        private const val GZIP_COMPRESSION = 2
        const val MAX_VALID_ARCHIVE_LENGTH = 1000000
    }
}

