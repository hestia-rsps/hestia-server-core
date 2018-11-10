package world.gregs.hestia.network.client

interface ConnectionChange {
    fun connect()

    fun disconnect()
}