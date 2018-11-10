package world.gregs.hestia.services

import com.alex.store.Index
import com.alex.store.Store
import com.alex.util.whirlpool.Whirlpool
import io.netty.buffer.Unpooled
import world.gregs.hestia.Settings
import java.io.IOException

object Cache {

    private var STORE: Store? = null//TODO change to another file system, Cache.kt doesn't need to be an object.
    private lateinit var versionTable: ByteArray

    @Throws(IOException::class)
    fun init(path: String? = null) {
        STORE = Store(path ?: Settings.get("cachePath", null) ?: throw NullPointerException())
        versionTable = createVersionTable()
    }

    fun getFile(index: Int, archive: Int): ByteArray {
        if(index == 255 && archive == 255) {
            return versionTable
        }

        if(index == 255) {
            return STORE?.index255?.getArchiveData(archive) ?: byteArrayOf()
        }

        return getIndex(index)?.mainFile?.getArchiveData(archive) ?: byteArrayOf()
    }

    fun getIndex(index: Int): Index? {
        return STORE?.indexes?.get(index)
    }

    private fun createVersionTable(): ByteArray {
        val buffer = Unpooled.directBuffer()
        STORE!!.run {
            buffer.writeByte(indexes.size)
            for (i in 0 until indexes.size) {
                buffer.writeInt(indexes[i]?.crc ?: 0)
                buffer.writeInt(indexes[i]?.table?.revision ?: 0)
                buffer.writeBytes(indexes[i]?.whirlpool ?: ByteArray(64))
            }
        }

        //Clone data
        val mainFileData = ByteArray(buffer.writerIndex())
        buffer.readBytes(mainFileData)

        //Write whirlpool hash
        buffer.resetReaderIndex()
        buffer.writeByte(0)
        buffer.writeBytes(Whirlpool.getHash(mainFileData, 0, mainFileData.size))

        //Extra data for CacheArchiveData
        val extra = Unpooled.directBuffer(5)
        extra.writeByte(0)
        extra.writeInt(buffer.readableBytes())

        //Cloned to remove excess data
        val bytes = ByteArray(buffer.readableBytes() + extra.readableBytes())
        buffer.readBytes(bytes, extra.readableBytes(), buffer.readableBytes())
        extra.readBytes(bytes, 0, extra.readableBytes())
        return bytes
    }

    fun indexCount(): Int {
        return STORE?.indexes?.size ?: 0
    }
}