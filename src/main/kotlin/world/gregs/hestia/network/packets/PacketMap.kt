package world.gregs.hestia.network.packets

import java.util.*

class PacketMap : HashMap<Int, Pair<InboundPacket, Int>>() {

    fun getPacket(id: Int): InboundPacket? {
        return this[id]?.first
    }

    fun getSize(packet: Int): Int? {
        return this[packet]?.second
    }

}