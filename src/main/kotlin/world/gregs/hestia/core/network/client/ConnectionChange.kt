package world.gregs.hestia.core.network.client

interface ConnectionChange {
    fun connect()

    fun disconnect()
}