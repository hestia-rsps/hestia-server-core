package world.gregs.hestia.core.services.load

import java.util.*

class PacketMap<T>: HashMap<Int, Pair<T, Int>>() {

    fun getPacket(id: Int): T? {
        return this[id]?.first
    }

    fun getSize(packet: Int): Int? {
        return this[packet]?.second
    }

}