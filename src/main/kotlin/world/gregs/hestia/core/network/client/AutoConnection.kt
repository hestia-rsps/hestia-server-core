package world.gregs.hestia.core.network.client

import world.gregs.hestia.core.network.pipe.Pipeline
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class AutoConnection(pipeline: Pipeline, private val host: String, private val port: Int, delay: Long = 10, unit: TimeUnit = TimeUnit.SECONDS) : ConnectionChange {
    private val client = Connection(pipeline)
    private val executor = Executors.newSingleThreadScheduledExecutor()
    var connected = false
        private set

    init {
        executor.scheduleWithFixedDelay({
            if(!connected) {
                attempt()
                val future = client.future
                if(future != null) {
                    future.channel().closeFuture().sync()
                    disconnect()
                }
            }
        }, 0, delay, unit)
    }

    private fun attempt() {
        val connected = try {
            client.start(host, port)
            true
        } catch (e: Exception) {
            //Connection refused
            false
        }

        if(connected) {
            connect()
        } else {
            disconnect()
        }
    }

    override fun connect() {
        connected = true
    }

    override fun disconnect() {
        client.finish()
        connected = false
    }
}