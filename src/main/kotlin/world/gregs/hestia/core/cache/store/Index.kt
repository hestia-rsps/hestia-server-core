package world.gregs.hestia.core.cache.store

import world.gregs.hestia.core.cache.crypto.CRC
import world.gregs.hestia.core.cache.crypto.Whirlpool
import java.nio.Buffer
import java.nio.ByteBuffer

class Index(private val index255: MainFile, val mainFile: MainFile) {
    var table: ReferenceTable? = null
        private set
    private var cachedFiles: Array<Array<ByteArray?>?>? = null
    var crc: Int = 0
        private set
    var whirlpool: ByteArray? = null
        private set

    val lastArchiveId: Int
        get() = table!!.archives!!.size - 1

    val validArchivesCount: Int
        get() = table!!.validArchiveIds!!.size

    val id: Int
        get() = mainFile.id

    init {
        val archiveData = index255.getArchiveData(id)
        if (archiveData != null) {
            crc = CRC.getHash(archiveData)
            whirlpool = Whirlpool.whirlpool(archiveData, 0, archiveData.size)
            val archive = Archive(id, archiveData, null)
            table = ReferenceTable(archive)
            resetCachedFiles()
        }
    }

    fun resetCachedFiles() {
        cachedFiles = arrayOfNulls(lastArchiveId + 1)
    }

    fun getLastFileId(archiveId: Int): Int {
        return if (!archiveExists(archiveId)) -1 else table!!.archives!![archiveId]!!.files!!.size - 1
    }

    fun getValidFilesCount(archiveId: Int): Int {
        return if (!archiveExists(archiveId)) -1 else table!!.archives!![archiveId]!!.validFileIds!!.size
    }

    fun archiveExists(archiveId: Int): Boolean {
        val archives = table!!.archives
        return archives!!.size > archiveId && archives[archiveId] != null
    }

    fun fileExists(archiveId: Int, fileId: Int): Boolean {
        return if (!archiveExists(archiveId)) {
            false
        } else {
            val files = table!!.archives!![archiveId]!!.files
            files!!.size > fileId && files[fileId] != null
        }
    }

    fun getArchiveId(name: String): Int {
        val nameHash = getNameHash(name)
        val archives = table!!.archives
        val validArchiveIds = table!!.validArchiveIds

        for (index in validArchiveIds!!.indices) {
            val archiveId = validArchiveIds[index]
            if (archives!![archiveId]!!.nameHash == nameHash) {
                return archiveId
            }
        }

        return -1
    }

    fun getFileId(archiveId: Int, name: String): Int {
        if (!archiveExists(archiveId)) {
            return -1
        } else {
            val nameHash = getNameHash(name)
            val files = table!!.archives!![archiveId]!!.files
            val validFileIds = table!!.archives!![archiveId]!!.validFileIds

            for (index in validFileIds!!.indices) {
                val fileId = validFileIds[index]
                if (files!![fileId]!!.nameHash == nameHash) {
                    return fileId
                }
            }

            return -1
        }
    }

    fun getFile(archiveId: Int): ByteArray? {
        return if (!archiveExists(archiveId)) null else getFile(archiveId, table!!.archives!![archiveId]!!.validFileIds!![0])
    }

    fun getFile(archiveId: Int, keys: IntArray?): ByteArray? {
        return if (!archiveExists(archiveId)) null else getFile(archiveId, table!!.archives!![archiveId]!!.validFileIds!![0], keys)
    }

    fun getFile(archiveId: Int, fileId: Int): ByteArray? {
        return getFile(archiveId, fileId, null)
    }

    fun getFile(archiveId: Int, fileId: Int, keys: IntArray?): ByteArray? {
        return try {
            if (!fileExists(archiveId, fileId)) {
                null
            } else {
                if (cachedFiles!![archiveId] == null || cachedFiles!![archiveId]!![fileId] == null) {
                    cacheArchiveFiles(archiveId, keys)
                }

                val file = cachedFiles!![archiveId]!![fileId]
                cachedFiles!![archiveId]!![fileId] = null
                file
            }
        } catch (var5: OutOfMemoryError) {
            null
        }

    }

    private fun cacheArchiveFiles(archiveId: Int, keys: IntArray?) {
        val archive = getArchive(archiveId, keys)
        val lastFileId = getLastFileId(archiveId)
        cachedFiles!![archiveId] = arrayOfNulls(lastFileId + 1)
        if (archive != null) {
            val data = archive.data
            if (data != null) {
                val filesCount = getValidFilesCount(archiveId)
                if (filesCount == 1) {
                    cachedFiles!![archiveId]!![lastFileId] = data
                } else {
                    var readPosition = data.size
                    --readPosition
                    val amtOfLoops = data[readPosition].toInt() and 255
                    readPosition -= amtOfLoops * filesCount * 4
                    val buffer = ByteBuffer.wrap(data)
                    (buffer as Buffer).position(readPosition)//Java 8 compatibility
                    val filesSize = IntArray(filesCount)

                    var sourceOffset: Int
                    var count: Int
                    for (loop in 0 until amtOfLoops) {
                        sourceOffset = 0

                        count = 0
                        while (count < filesCount) {
                            sourceOffset += buffer.int
                            filesSize[count] += sourceOffset
                            ++count
                        }
                    }

                    val filesData = arrayOfNulls<ByteArray>(filesCount)

                    sourceOffset = 0
                    while (sourceOffset < filesCount) {
                        filesData[sourceOffset] = ByteArray(filesSize[sourceOffset])
                        filesSize[sourceOffset] = 0
                        ++sourceOffset
                    }

                    (buffer as Buffer).position(readPosition)
                    sourceOffset = 0

                    var fileId: Int
                    var i: Int
                    count = 0
                    while (count < amtOfLoops) {
                        fileId = 0

                        i = 0
                        while (i < filesCount) {
                            fileId += buffer.int
                            System.arraycopy(data, sourceOffset, filesData[i], filesSize[i], fileId)
                            sourceOffset += fileId
                            filesSize[i] += fileId
                            ++i
                        }
                        ++count
                    }

                    count = 0
                    val var17: IntArray = table!!.archives!![archiveId]!!.validFileIds!!
                    val var16 = var17.size

                    i = 0
                    while (i < var16) {
                        fileId = var17[i]
                        cachedFiles!![archiveId]!![fileId] = filesData[count++]
                        ++i
                    }
                }

            }
        }
    }

    fun getArchive(id: Int): Archive? {
        return mainFile.getArchive(id, null)
    }

    fun getArchive(id: Int, keys: IntArray?): Archive? {
        return mainFile.getArchive(id, keys)
    }

    companion object {


        /**
         * Returns string name hash
         */
        fun getNameHash(name: String): Int {
            var hash = 0
            val case = name.toLowerCase()
            for (index in 0 until name.length) {
                hash = replaceSymbols(case[index]) + ((hash shl 5) - hash)
            }
            return hash
        }

        /**
         * Replaces some symbols with most of one of the Japanese alphabets (Katakana)?
         */
        private fun replaceSymbols(c: Char): Int {
            val char = c.toInt()
            return if(char in 1..127 || char in 160..255) {
                char
            } else {
                when(char) {
                    8364 -> -128
                    8218 -> -126
                    402 -> -125
                    8222 -> -124
                    8230 -> -123
                    8224 -> -122
                    8225 -> -121
                    710 -> -120
                    8240 -> -119
                    352 -> -118
                    8249 -> -117
                    338 -> -116
                    381 -> -114
                    8216 -> -111
                    8217 -> -110
                    8220 -> -109
                    8221 -> -108
                    8226 -> -107
                    8211 -> -106
                    8212 -> -105
                    732 -> -104
                    8482 -> -103
                    353 -> -102
                    8250 -> -101
                    339 -> -100
                    382 -> -98
                    376 -> -97
                    else -> 63
                }
            }
        }
    }
}

