package world.gregs.hestia.core.network.server.threads

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

abstract class CoreThreadFactory(name: String, private val priority: Int) : ThreadFactory {
    private val group: ThreadGroup
    private val threadNumber = AtomicInteger(1)
    private val namePrefix: String

    init {
        val s = System.getSecurityManager()
        group = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
        namePrefix = "$name Pool-${poolNumber()}-thread-"
    }

    override fun newThread(r: Runnable): Thread {
        val thread = Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0)
        thread.isDaemon = false
        thread.priority = priority
        return thread
    }

    abstract fun poolNumber(): Int
}
