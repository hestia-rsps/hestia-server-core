package world.gregs.hestia.core.cache.store

import java.nio.ByteBuffer

class ReferenceTable(private val archive: Archive) {
    var revision: Int = 0
        private set
    var isNamed: Boolean = false
        private set
    private var usesWhirlpool: Boolean = false
    var archives: Array<ArchiveReference?>? = null
        private set
    var validArchiveIds: IntArray? = null
        private set

    init {
        decodeHeader()
    }

    private fun decodeHeader() {
        val buffer = ByteBuffer.wrap(archive.data)
        val protocol = buffer.getUByte()
        if (protocol in 5..7) {
            if (protocol >= 6) {
                revision = buffer.int
            }

            val hash = buffer.getUByte()
            isNamed = 1 and hash != 0
            usesWhirlpool = 2 and hash != 0
            val validArchivesCount = buffer.getProtocol(protocol)
            validArchiveIds = IntArray(validArchivesCount)
            var lastArchiveId = 0
            var biggestArchiveId = 0

            var index: Int
            var lastFileId: Int
            index = 0
            while (index < validArchivesCount) {
                lastArchiveId += buffer.getProtocol(protocol)
                lastFileId = lastArchiveId
                if (lastFileId > biggestArchiveId) {
                    biggestArchiveId = lastFileId
                }

                validArchiveIds!![index] = lastFileId
                ++index
            }

            archives = arrayOfNulls(biggestArchiveId + 1)

            index = 0
            while (index < validArchivesCount) {
                archives!![validArchiveIds!![index]] = ArchiveReference()
                ++index
            }

            if (isNamed) {
                index = 0
                while (index < validArchivesCount) {
                    archives!![validArchiveIds!![index]]!!.nameHash = buffer.int
                    ++index
                }
            }

            if (usesWhirlpool) {
                index = 0
                while (index < validArchivesCount) {
                    val whirlpool = ByteArray(64)
                    buffer.get(whirlpool, 0, 64)
                    archives!![validArchiveIds!![index]]!!.whirlpool = whirlpool
                    ++index
                }
            }

            index = 0
            while (index < validArchivesCount) {
                archives!![validArchiveIds!![index]]!!.crc = buffer.int
                ++index
            }

            index = 0
            while (index < validArchivesCount) {
                archives!![validArchiveIds!![index]]!!.revision = buffer.int
                ++index
            }

            index = 0
            while (index < validArchivesCount) {
                archives!![validArchiveIds!![index]]!!.validFileIds = IntArray(buffer.getProtocol(protocol))
                ++index
            }

            var index2: Int
            index = 0
            while (index < validArchivesCount) {
                lastFileId = 0
                val archive = archives!![validArchiveIds!![index]]!!

                var lastFile = -1
                index2 = 0
                while (index2 < archive.validFileIds!!.size) {
                    lastFileId += buffer.getProtocol(protocol)
                    if (lastFileId > lastFile) {
                        lastFile = lastFileId
                    }

                    archive.validFileIds!![index2] = lastFileId
                    ++index2
                }

                archive.files = arrayOfNulls(lastFile + 1)

                index2 = 0
                while (index2 < archive.validFileIds!!.size) {
                    archive.files!![archive.validFileIds!![index2]] = FileReference()
                    ++index2
                }
                ++index
            }

            if (isNamed) {
                index = 0
                while (index < validArchivesCount) {
                    val archive = archives!![validArchiveIds!![index]]!!

                    index2 = 0
                    while (index2 < archive.validFileIds!!.size) {
                        archive.files!![archive.validFileIds!![index2]]!!.nameHash = buffer.int
                        ++index2
                    }
                    ++index
                }
            }

        } else {
            throw RuntimeException("INVALID PROTOCOL")
        }
    }

    private fun ByteBuffer.getProtocol(protocol: Int): Int {
        return if (protocol >= 7) getBigSmart() else getUShort()
    }

    private fun ByteBuffer.getBigSmart(): Int {
        return if (get(position()) >= 0) getUShort() else Int.MAX_VALUE and int
    }
}

fun ByteBuffer.getUByte(): Int {
    return get().toInt() and 0xff
}

fun ByteBuffer.getUShort(): Int {
    return short.toInt() and 0xffff
}

