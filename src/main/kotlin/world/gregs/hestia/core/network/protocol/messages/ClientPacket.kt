package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.codec.packet.Packet

/**
 * Client packet to be redirected to the login/social server (identified as a social packet)
 * @param entity The player's entity id
 * @param packet The packet to send
 * @param handshake Whether login-handshake is complete
 */
data class ClientPacket(val entity: Int, val packet: Packet, val handshake: Boolean) : Message//TODO convert to message?