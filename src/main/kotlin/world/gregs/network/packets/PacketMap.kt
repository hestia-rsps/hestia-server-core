package world.gregs.network.packets

import world.gregs.network.packets.InboundPacket
import java.util.*

class PacketMap : HashMap<Int, Pair<InboundPacket, Int>>() {

    fun getPacket(id: Int): InboundPacket? {
        return this[id]?.first
    }

    fun getSize(packet: Int): Int? {
        return this[packet]?.second
    }

}