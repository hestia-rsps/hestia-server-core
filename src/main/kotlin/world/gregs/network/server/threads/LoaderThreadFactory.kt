package world.gregs.network.server.threads

import java.util.concurrent.atomic.AtomicInteger

class LoaderThreadFactory : CoreThreadFactory("Loader", Thread.NORM_PRIORITY) {

    override fun poolNumber(): Int {
        return poolNumber.getAndIncrement()
    }


    companion object {
        private val poolNumber = AtomicInteger(1)
    }

}