package world.gregs.network.client

interface ConnectionChange {
    fun connect()

    fun disconnect()
}