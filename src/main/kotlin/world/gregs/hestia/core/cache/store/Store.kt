package world.gregs.hestia.core.cache.store


import java.io.IOException
import java.io.RandomAccessFile

class Store @Throws(IOException::class) constructor(path: String, newProtocol: Boolean = true) {

    val indexes: Array<Index>
    val index255: MainFile
    private val data: RandomAccessFile = RandomAccessFile(path + "main_file_cache.dat2", "rw")
    private val readCachedBuffer: ByteArray = ByteArray(520)

    init {
        index255 = MainFile(255, data, RandomAccessFile(path + "main_file_cache.idx255", "rw"), readCachedBuffer, newProtocol)
        val indices = index255.archivesCount
        val indexes = arrayOfNulls<Index>(indices)

        for (id in 0 until indices) {
            val index = Index(index255, MainFile(id, data, RandomAccessFile(path + "main_file_cache.idx" + id, "rw"), readCachedBuffer, newProtocol))
            if (index.table != null) {
                indexes[id] = index
            }
        }
        this.indexes = indexes.filterNotNull().toTypedArray()
    }
}

