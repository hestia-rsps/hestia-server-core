package world.gregs.hestia.core.network.codec.decode

import io.netty.buffer.ByteBuf
import world.gregs.hestia.core.cache.crypto.Cipher

/**
 * Decodes opcode using [cipher] before processing packet
 */
abstract class IsaacPacketDecoder(internal val cipher: Cipher) : PacketDecoder() {

    override fun readOpcode(buf: ByteBuf): Int {
        return (buf.readUnsignedByte().toInt() - cipher.nextInt()) and 0xff
    }

}