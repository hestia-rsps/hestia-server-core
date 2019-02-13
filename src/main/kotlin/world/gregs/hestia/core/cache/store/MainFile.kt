package world.gregs.hestia.core.cache.store

import world.gregs.hestia.core.cache.store.Archive.Companion.MAX_VALID_ARCHIVE_LENGTH
import java.io.IOException
import java.io.RandomAccessFile

class MainFile(val id: Int, private val data: RandomAccessFile, private val index: RandomAccessFile, private val readCachedBuffer: ByteArray, private val newProtocol: Boolean) {

    val archivesCount: Int
        @Throws(IOException::class)
        get() = synchronized(index) {
            return (index.length() / 6L).toInt()
        }

    fun getArchive(id: Int): Archive? {
        return getArchive(id, null)
    }

    fun getArchive(id: Int, keys: IntArray?): Archive? {
        val data = getArchiveData(id)
        return if (data == null) null else Archive(id, data, keys)
    }

    fun getArchiveData(id: Int): ByteArray? {
        synchronized(data) {
            try {
                if (index.length() < (6 * id + 6).toLong()) {
                    return null
                } else {
                    index.seek((6 * id).toLong())
                    index.read(readCachedBuffer, 0, 6)
                    val archiveLength = (readCachedBuffer[2].toInt() and 255) + (255 and readCachedBuffer[0].toInt() shl 16) + (readCachedBuffer[1].toInt() shl 8 and '\uff00'.toInt())
                    var sector = (readCachedBuffer[3].toInt() and 255 shl 16) - (-('\uff00'.toInt() and (readCachedBuffer[4].toInt() shl 8)) - (readCachedBuffer[5].toInt() and 255))
                    if (archiveLength < 0 || archiveLength > MAX_VALID_ARCHIVE_LENGTH) {
                        return null
                    } else if (sector <= 0 || data.length() / 520L < sector.toLong()) {
                        return null
                    } else {
                        val archive = ByteArray(archiveLength)
                        var readBytesCount = 0

                        var nextSector: Int
                        var part = 0
                        while (archiveLength > readBytesCount) {
                            if (sector == 0) {
                                return null
                            }

                            data.seek((520 * sector).toLong())
                            var dataBlockSize = archiveLength - readBytesCount
                            val headerSize: Byte
                            val currentIndex: Int
                            val currentPart: Int
                            val currentArchive: Int
                            if (65535 < id && newProtocol) {
                                headerSize = 10
                                if (dataBlockSize > 510) {
                                    dataBlockSize = 510
                                }

                                data.read(readCachedBuffer, 0, headerSize + dataBlockSize)
                                currentArchive = (readCachedBuffer[1].toInt() and 255 shl 16) + (readCachedBuffer[0].toInt() and 255 shl 24) + (('\uff00'.toInt() and (readCachedBuffer[2].toInt() shl 8)) - -(readCachedBuffer[3].toInt() and 255))
                                currentPart = (readCachedBuffer[4].toInt() and 255 shl 8) + (255 and readCachedBuffer[5].toInt())
                                nextSector = (readCachedBuffer[8].toInt() and 255) + ('\uff00'.toInt() and (readCachedBuffer[7].toInt() shl 8)) + (255 and readCachedBuffer[6].toInt() shl 16)
                                currentIndex = readCachedBuffer[9].toInt() and 255
                            } else {
                                headerSize = 8
                                if (dataBlockSize > 512) {
                                    dataBlockSize = 512
                                }

                                data.read(readCachedBuffer, 0, headerSize + dataBlockSize)
                                currentArchive = (255 and readCachedBuffer[1].toInt()) + ('\uff00'.toInt() and (readCachedBuffer[0].toInt() shl 8))
                                currentPart = (readCachedBuffer[2].toInt() and 255 shl 8) + (255 and readCachedBuffer[3].toInt())
                                nextSector = (readCachedBuffer[6].toInt() and 255) + ('\uff00'.toInt() and (readCachedBuffer[5].toInt() shl 8)) + (255 and readCachedBuffer[4].toInt() shl 16)
                                currentIndex = readCachedBuffer[7].toInt() and 255
                            }

                            if (newProtocol && id != currentArchive || currentPart != part || this.id != currentIndex) {
                                return null
                            }

                            if (nextSector < 0 || data.length() / 520L < nextSector.toLong()) {
                                return null
                            }

                            var index = headerSize.toInt()
                            while (dataBlockSize + headerSize > index) {
                                archive[readBytesCount++] = readCachedBuffer[index]
                                ++index
                            }

                            ++part
                            sector = nextSector
                        }

                        return archive
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }
    }
}

