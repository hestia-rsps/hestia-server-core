package world.gregs.hestia.core.network.codec.debug

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.HandshakeCodec
import world.gregs.hestia.core.network.codec.message.MessageHandshake
import world.gregs.hestia.core.network.codec.message.SimpleMessageHandshakeDecoder
import world.gregs.hestia.core.network.codec.packet.Packet

open class DebugMessageHandshakeDecoder(codec: HandshakeCodec, handshake: MessageHandshake) : SimpleMessageHandshakeDecoder(codec, handshake) {

    override fun decode(ctx: ChannelHandlerContext, msg: Packet, out: MutableList<Any>) {
        logger.debug("Decoding ${msg.opcode} $msg ${handshake.shook(ctx)}")
        super.decode(ctx, msg, out)
    }
}