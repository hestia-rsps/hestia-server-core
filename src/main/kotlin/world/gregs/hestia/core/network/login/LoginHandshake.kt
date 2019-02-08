package world.gregs.hestia.core.network.login

import io.netty.buffer.ByteBuf
import io.netty.util.AttributeKey
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.Session
import world.gregs.hestia.core.network.codec.inbound.HandshakeHandler
import world.gregs.hestia.core.network.packets.InboundPacket
import world.gregs.hestia.core.network.packets.Packet
import world.gregs.hestia.core.network.packets.PacketInfo
import world.gregs.hestia.core.network.packets.out.Response
import world.gregs.hestia.core.services.Decryption

/**
 * LoginHandshake
 * Implementation of [HandshakeHandler] for login requests
 * Processes a single login [packet] calls [listener] with the request data
 */
open class LoginHandshake(private val packet: InboundPacket, private val listener: LoginRequestListener) : HandshakeHandler(), LoginDecoder<InboundPacket> {
    override val key = AttributeKey.valueOf<Boolean>("login.handshake")!!
    override val logger = LoggerFactory.getLogger(LoginHandshake::class.java)!!

    override fun getHandler(opcode: Int): InboundPacket? {
        val annotation = packet::class.java.getAnnotation(PacketInfo::class.java) ?: return null
        return if(annotation.opcodes.contains(opcode)) {
            packet
        } else {
            null
        }
    }

    override fun process(session: Session, buffer: ByteBuf) {
        decode(session, buffer)
    }

    override fun handshake(session: Session, buffer: Packet) {
        session.respond(Response.DATA_CHANGE)
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

        listener.login(session, handler, packet, password, serverSeed, clientSeed)
    }

}