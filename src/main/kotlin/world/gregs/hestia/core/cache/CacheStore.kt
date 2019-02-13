package world.gregs.hestia.core.cache

import io.netty.buffer.Unpooled
import world.gregs.hestia.core.Settings
import world.gregs.hestia.core.cache.crypto.Whirlpool
import world.gregs.hestia.core.cache.store.Index
import world.gregs.hestia.core.cache.store.Store

class CacheStore @Throws(IllegalArgumentException::class) constructor(path: String = Settings.getString("cachePath") ?: throw IllegalArgumentException()) : Cache {

    private val store = Store(path)
    private var versionTable = createVersionTable()

    override fun getFile(index: Int, archive: Int, file: Int): ByteArray? {
        return getIndex(index).getFile(archive, file)
    }

    override fun getFile(index: Int, archive: Int): ByteArray? {
        return getIndex(index).getFile(archive)
    }

    override fun getArchive(index: Int, archive: Int): ByteArray? {
        if(index == 255 && archive == 255) {
            return versionTable
        }

        if(index == 255) {
            return store.index255.getArchiveData(archive)
        }

        return getIndex(index).mainFile.getArchiveData(archive)
    }

    override fun getIndex(index: Int): Index {
        return store.indexes[index]
    }

    override fun indexCount(): Int {
        return store.indexes.size
    }

    private fun createVersionTable(): ByteArray {
        val buffer = Unpooled.directBuffer()
        store.run {
            buffer.writeByte(indexes.size)
            for (i in 0 until indexes.size) {
                buffer.writeInt(indexes[i].crc)
                buffer.writeInt(indexes[i].table?.revision ?: 0)
                buffer.writeBytes(indexes[i].whirlpool ?: ByteArray(64))
            }
        }

        //Clone data
        val mainFileData = ByteArray(buffer.writerIndex())
        buffer.readBytes(mainFileData)

        //Write whirlpool hash
        buffer.resetReaderIndex()
        buffer.writeByte(0)
        buffer.writeBytes(Whirlpool.whirlpool(mainFileData, 0, mainFileData.size))

        //Extra data for CacheArchiveData
        val extra = Unpooled.directBuffer(5)
        extra.writeByte(0)
        extra.writeInt(buffer.readableBytes())

        //Cloned to remove excess data
        val bytes = ByteArray(buffer.readableBytes() + extra.readableBytes())
        buffer.readBytes(bytes, extra.readableBytes(), buffer.readableBytes())
        extra.readBytes(bytes, 0, extra.readableBytes())
        buffer.release()
        extra.release()
        return bytes
    }
}