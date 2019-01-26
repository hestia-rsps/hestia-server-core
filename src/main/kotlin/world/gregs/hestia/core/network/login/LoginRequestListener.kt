package world.gregs.hestia.core.network.login

import world.gregs.hestia.core.network.Session
import world.gregs.hestia.core.network.packets.InboundPacket
import world.gregs.hestia.core.network.packets.Packet

/**
 * LoginRequestListener
 * Processes a lobby or game login request
 */
interface LoginRequestListener {
    fun login(session: Session, handler: InboundPacket, packet: Packet, password: String, serverSeed: Long, clientSeed: Long)
}