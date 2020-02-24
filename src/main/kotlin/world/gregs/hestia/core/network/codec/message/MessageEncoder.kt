package world.gregs.hestia.core.network.codec.message

import io.netty.buffer.ByteBuf
import world.gregs.hestia.cache.crypto.Cipher
import world.gregs.hestia.core.network.packet.PacketBuilder
import world.gregs.hestia.core.network.packet.PacketWriter

abstract class MessageEncoder<T : Message> {

    /**
     * Encodes data from a [Message] into a [Packet]
     * @param builder The packet to encode the data into
     * @param message The message with the data to encode
     */
    abstract fun encode(builder: PacketBuilder, message: T)

    /**
     * Writes encoded message to outbound buffer
     * @param message The message to encode
     * @param out The buffer to write the encoded data too
     * @param cipher The isaac cipher to encrypt with
     */
    open fun write(message: T, out: ByteBuf, cipher: Cipher? = null) {
        //Wrap the out buffer in a packet builder
        val packet = PacketWriter(buffer = out, cipher = cipher)
        //Encode the message into the packet buffer
        encode(packet, message)
        //Finish writing the packet
        packet.writeSize()
    }

}