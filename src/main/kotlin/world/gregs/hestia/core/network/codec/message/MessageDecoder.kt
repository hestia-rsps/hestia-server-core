package world.gregs.hestia.core.network.codec.message

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.network.packet.Packet

abstract class MessageDecoder<T : Message>(val size: Int, vararg val opcodes: Int) {

    constructor(type: Packet.Type, vararg opcodes: Int) : this(type.int, *opcodes)

    /**
     * Decodes data from packet into a [Message]
     * @param ctx The channel the packet is from
     * @param packet The packet to decode
     * @return A [Message] containing the decoded data
     */
    abstract fun decode(ctx: ChannelHandlerContext, packet: Packet): T?

    /**
     * Decodes packet into message and writes to [out]
     * @param ctx The channel the packet is from
     * @param msg The packet to decode using [decode]
     * @param out The outbound messages
     */
    fun write(ctx: ChannelHandlerContext, msg: Packet, out: MutableList<Any>) {
        val message = decode(ctx, msg)
        msg.release()
        if(message != null) {
            out.add(message)
        }
    }
}