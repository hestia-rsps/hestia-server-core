package world.gregs.hestia.core.network.protocol

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.cache.crypto.Rsa
import world.gregs.hestia.core.cache.crypto.Xtea
import world.gregs.hestia.core.network.NetworkConstants
import world.gregs.hestia.core.network.client.Response
import world.gregs.hestia.core.network.clientRespond
import world.gregs.hestia.core.network.codec.packet.Packet
import world.gregs.hestia.core.network.codec.packet.PacketReader

/**
 * LoginDecoder
 * Decodes the first half of the login message
 */
object LoginDecoder {

    /**
     * Decodes login message
     * @param ctx Channel
     * @param packet Packet to decode
     * @param extra Whether to read extra byte
     * @return Triple(password, server seed, client seed)
     */
    fun decode(ctx: ChannelHandlerContext, packet: Packet, extra: Boolean = false) : Triple<String, Long, Long>? {
        val version = packet.readInt()
        val build = packet.readInt()
        if (version != NetworkConstants.CLIENT_MAJOR_VERSION || build != NetworkConstants.CLIENT_MINOR_VERSION) {
            ctx.clientRespond(Response.GAME_UPDATED)
            return null
        }

        if (extra) {
            packet.readUnsignedByte()
        }

        val rsaBlockSize = packet.readUnsignedShort()//RSA block size
        if (rsaBlockSize > packet.readableBytes()) {
            ctx.clientRespond(Response.BAD_SESSION_ID)
            return null
        }
        val data = ByteArray(rsaBlockSize)
        packet.readBytes(data)
        val rsa = Rsa.decrypt(data)
        val rsaBuffer = PacketReader(rsa)
        val sessionId = rsaBuffer.readUnsignedByte()
        if (sessionId != 10) {//rsa block start check
            ctx.clientRespond(Response.BAD_SESSION_ID)
            return null
        }

        val isaacKeys = IntArray(4)
        for (i in isaacKeys.indices) {
            isaacKeys[i] = rsaBuffer.readInt()
        }

        if (rsaBuffer.readLong() != 0L) {//password should start here (marked by 0L)
            ctx.clientRespond(Response.BAD_SESSION_ID)
            return null
        }

        val password: String = rsaBuffer.readString()
        val serverSeed = rsaBuffer.readLong()
        val clientSeed = rsaBuffer.readLong()

        Xtea.decipher(packet.buffer, isaacKeys)
        return Triple(password, serverSeed, clientSeed)
    }
}