package world.gregs.hestia.core.cache

import world.gregs.hestia.core.Settings
import world.gregs.hestia.core.cache.crypto.Rsa
import world.gregs.hestia.core.cache.crypto.Whirlpool
import world.gregs.hestia.core.cache.store.Index
import world.gregs.hestia.core.cache.store.Store
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.math.BigInteger
import java.nio.Buffer
import java.nio.ByteBuffer

class CacheStore @Throws(IllegalArgumentException::class) constructor(path: String = Settings.getString("cachePath") ?: throw IllegalArgumentException(), modulus: BigInteger? = BigInteger(Settings.getString("rsaModulus"), 16), private: BigInteger? = BigInteger(Settings.getString("rsaPrivate"), 16)) : Cache {

    private val store = Store(path)
    private var versionTable = createVersionTable(true, modulus, private)

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

    private fun createVersionTable(whirlpool: Boolean, modulus: BigInteger?, private: BigInteger?): ByteArray {
        val bout = ByteArrayOutputStream()
        DataOutputStream(bout).use { buffer ->
            store.run {
                if(whirlpool) {
                    buffer.writeByte(indexes.size)
                }

                for (i in 0 until indexes.size) {
                    buffer.writeInt(indexes[i].crc)
                    buffer.writeInt(indexes[i].table?.revision ?: 0)
                    if(whirlpool) {
                        buffer.write(indexes[i].whirlpool ?: ByteArray(64))
                        //keys?
                    }
                }
            }

            if(whirlpool) {
                val bytes = bout.toByteArray()
                var temp = ByteBuffer.allocate(65)
                temp.put(1)
                temp.put(Whirlpool.whirlpool(bytes, 0, bytes.size))
                (temp as Buffer).flip()

                if (modulus != null && private != null) {
                    temp = Rsa.crypt(temp, modulus, private)
                }

                buffer.write(temp.array())
            }

            val data = bout.toByteArray()
            val out = ByteBuffer.allocate(5 + data.size)
            out.put(0)
            out.putInt(data.size)
            out.put(data)
            return out.array()
        }
    }
}