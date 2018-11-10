package world.gregs.hestia.core.network.server.threads

import java.util.concurrent.atomic.AtomicInteger

class DecoderThreadFactory : CoreThreadFactory("Decoder", Thread.MAX_PRIORITY - 1) {

    override fun poolNumber(): Int {
        return poolNumber.getAndIncrement()
    }


    companion object {
        private val poolNumber = AtomicInteger(1)
    }

}