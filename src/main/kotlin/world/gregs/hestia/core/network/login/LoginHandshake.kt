package world.gregs.hestia.core.network.login

import io.netty.buffer.ByteBuf
import world.gregs.hestia.core.network.Session
import world.gregs.hestia.core.network.codec.inbound.HandshakeHandler
import world.gregs.hestia.core.network.packets.InboundPacket
import world.gregs.hestia.core.network.packets.Packet
import world.gregs.hestia.core.services.load.PacketMap
import world.gregs.hestia.core.network.packets.out.Response
import world.gregs.hestia.core.services.Decryption

class LoginHandshake(private val packets: PacketMap, private val listener: LoginRequestListener) : HandshakeHandler(), LoginDecoder {

    override fun process(session: Session, buffer: ByteBuf) {
        decode(session, buffer, packets)
    }

    override fun handshake(session: Session, buffer: ByteBuf): Boolean {
        return if (validate(session, buffer, 14, 0)) {
            session.respond(Response.DATA_CHANGE)
            true
        } else {
            false
        }
    }

    override fun handle(session: Session, handler: InboundPacket, packet: Packet, rsaPacket: Packet, isaacKeys: IntArray) {
        if (rsaPacket.readLong() != 0L) {//password should start here (marked by 0L)
            session.respond(Response.BAD_SESSION_ID)
            return
        }

        val password: String = rsaPacket.readString()
        val serverSeed = rsaPacket.readLong()
        val clientSeed = rsaPacket.readLong()

        Decryption.decodeXTEA(packet, isaacKeys)

        login(session, handler, packet, password, serverSeed, clientSeed)
    }

    fun login(session: Session, handler: InboundPacket, packet: Packet, password: String, serverSeed: Long, clientSeed: Long) {
        listener.login(session, handler, packet, password, serverSeed, clientSeed)
    }

}