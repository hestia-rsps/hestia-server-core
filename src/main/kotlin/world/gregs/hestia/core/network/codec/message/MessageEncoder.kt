package world.gregs.hestia.core.network.codec.message

import io.netty.buffer.ByteBuf
import world.gregs.hestia.core.network.codec.packet.Packet
import world.gregs.hestia.core.network.codec.packet.PacketBuilder
import world.gregs.hestia.core.network.codec.packet.PacketWriter

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
     */
    fun write(message: T, out: ByteBuf) {
        //Wrap the out buffer in a packet builder
        val packet = PacketWriter(buffer = out)
        //Encode the message into the packet buffer
        encode(packet, message)
        //Finish writing the packet
        packet.writeSize()
    }

}