package world.gregs.hestia.core.cache.compress

import java.nio.ByteBuffer
import java.util.zip.Inflater

object GZip {
    private val INFLATER = Inflater(true)

    fun decompress(buffer: ByteBuffer, data: ByteArray) {
        synchronized(INFLATER) {
            if (buffer.array()[buffer.position()].toInt() == 31 && buffer.array()[buffer.position() + 1].toInt() == -117) {
                try {
                    INFLATER.setInput(buffer.array(), buffer.position() + 10, -buffer.position() - 18 + buffer.array().size)
                    INFLATER.inflate(data)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                INFLATER.reset()
            }
        }
    }
}

