package world.gregs.network.login

import world.gregs.network.NetworkConstants
import world.gregs.network.packets.out.Response
import world.gregs.network.Session
import world.gregs.network.packets.InboundPacket
import world.gregs.network.packets.Packet
import world.gregs.network.packets.PacketMap
import world.gregs.services.Decryption
import io.netty.buffer.ByteBuf
import org.slf4j.LoggerFactory

interface LoginDecoder {

    fun decode(session: Session, buffer: ByteBuf, packets: PacketMap) {
        val packet = Packet(buffer = buffer)
        val packetId = packet.readUnsignedByte()

        val handler = packets.getPacket(packetId)
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
        if (version != NetworkConstants.CLIENT_VERSION || build != NetworkConstants.CLIENT_BUILD) {
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

    fun handle(session: Session, handler: InboundPacket, packet: Packet, rsaPacket: Packet, isaacKeys: IntArray)

    companion object {
        private val logger = LoggerFactory.getLogger(LoginDecoder::class.java)
    }
}