package world.gregs.hestia.network.login

import world.gregs.hestia.network.Session
import world.gregs.hestia.network.packets.InboundPacket
import world.gregs.hestia.network.packets.Packet

interface LoginRequestListener {
    fun login(session: Session, handler: InboundPacket, packet: Packet, password: String, serverSeed: Long, clientSeed: Long)
}