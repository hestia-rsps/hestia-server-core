package world.gregs.hestia.core.cache.store

data class ArchiveReference(var nameHash: Int = 0,
                            var whirlpool: ByteArray? = null,
                            var crc: Int = 0,
                            var revision: Int = 0,
                            var files: Array<FileReference?>? = null,
                            var validFileIds: IntArray? = null) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArchiveReference

        if (nameHash != other.nameHash) return false
        if (whirlpool != null) {
            if (other.whirlpool == null) return false
            if (!whirlpool!!.contentEquals(other.whirlpool!!)) return false
        } else if (other.whirlpool != null) return false
        if (crc != other.crc) return false
        if (revision != other.revision) return false
        if (files != null) {
            if (other.files == null) return false
            if (!files!!.contentEquals(other.files!!)) return false
        } else if (other.files != null) return false
        if (validFileIds != null) {
            if (other.validFileIds == null) return false
            if (!validFileIds!!.contentEquals(other.validFileIds!!)) return false
        } else if (other.validFileIds != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nameHash
        result = 31 * result + (whirlpool?.contentHashCode() ?: 0)
        result = 31 * result + crc
        result = 31 * result + revision
        result = 31 * result + (files?.contentHashCode() ?: 0)
        result = 31 * result + (validFileIds?.contentHashCode() ?: 0)
        return result
    }
}

