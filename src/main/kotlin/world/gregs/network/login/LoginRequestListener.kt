package world.gregs.network.login

import world.gregs.network.Session
import world.gregs.network.packets.InboundPacket
import world.gregs.network.packets.Packet

interface LoginRequestListener {
    fun login(session: Session, handler: InboundPacket, packet: Packet, password: String, serverSeed: Long, clientSeed: Long)
}