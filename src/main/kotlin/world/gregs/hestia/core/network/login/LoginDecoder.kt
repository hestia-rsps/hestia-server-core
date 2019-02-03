package world.gregs.hestia.core.network.login

import io.netty.buffer.ByteBuf
import org.slf4j.Logger
import world.gregs.hestia.core.network.NetworkConstants
import world.gregs.hestia.core.network.Session
import world.gregs.hestia.core.network.packets.Packet
import world.gregs.hestia.core.network.packets.out.Response
import world.gregs.hestia.core.services.Decryption

interface LoginDecoder<T> {

    val logger: Logger

    fun getHandler(opcode: Int): T?

    fun decode(session: Session, buffer: ByteBuf) {
        val packet = Packet(buffer = buffer)
        val packetId = packet.readUnsignedByte()

        val handler = getHandler(packetId)
        if(handler == null) {
            logger.warn("Unhandled login request: $packetId ${packet.readableBytes()}")
            session.close()
            return
        }

        packet.opcode = packetId

        val packetSize = packet.readShort()
        if (packetSize != packet.readableBytes()) {
            logger.info("Invalid packet size $packetSize ${packet.readableBytes()}")
            session.close()
            return
        }

        val version = packet.readInt()
        val build = packet.readInt()
        if (version != NetworkConstants.CLIENT_MAJOR_VERSION || build != NetworkConstants.CLIENT_MINOR_VERSION) {
            logger.info("Invalid game version $version $build")
            session.respond(Response.GAME_UPDATED)
            return
        }

        if (packetId == 16) {
            packet.readUnsignedByte()
        }

        val rsaBlockSize = packet.readShort()//RSA block size
        if (rsaBlockSize > packet.readableBytes()) {
            session.respond(Response.BAD_SESSION_ID)
            return
        }
        val data = ByteArray(rsaBlockSize)
        packet.readBytes(data)
        val rsaBuffer = Packet(Decryption.decryptRsa(data, NetworkConstants.RSA_EXPONENT, NetworkConstants.RSA_MODULUS))
        val sessionId = rsaBuffer.readUnsignedByte()
        if (sessionId != 10) {//rsa block start check
            session.respond(Response.BAD_SESSION_ID)
            return
        }

        val isaacKeys = IntArray(4)
        for (i in isaacKeys.indices) {
            isaacKeys[i] = rsaBuffer.readInt()
        }

        handle(session, handler, packet, rsaBuffer, isaacKeys)
    }

    fun handle(session: Session, handler: T, packet: Packet, rsaPacket: Packet, isaacKeys: IntArray)
}