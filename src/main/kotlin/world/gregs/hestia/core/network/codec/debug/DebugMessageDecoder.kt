package world.gregs.hestia.core.network.codec.debug

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.Codec
import world.gregs.hestia.core.network.codec.message.SimpleMessageDecoder
import world.gregs.hestia.core.network.packet.Packet

open class DebugMessageDecoder(codec: Codec) : SimpleMessageDecoder(codec) {

    override fun decode(ctx: ChannelHandlerContext, msg: Packet, out: MutableList<Any>) {
        logger.debug("Decoding ${msg.opcode} $msg")
        super.decode(ctx, msg, out)
    }
}