package world.gregs.hestia.core.network.codec.message

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.codec.HandshakeCodec
import world.gregs.hestia.core.network.codec.packet.Packet
import java.io.IOException

/**
 * Decodes [Packet]s into [Message]s using decoders found in the supplied codec
 * @param handshake The handshake key
 * @param codec The codec containing decoders to process
 */
@ChannelHandler.Sharable
open class SimpleMessageHandshakeDecoder(protected val codec: HandshakeCodec, protected val handshake: MessageHandshake) : MessageToMessageDecoder<Packet>() {

    internal val logger = LoggerFactory.getLogger(this::class.java)!!

    @Suppress("UNCHECKED_CAST")
    override fun decode(ctx: ChannelHandlerContext, msg: Packet, out: MutableList<Any>) {
        val handshake = handshake.shook(ctx)
        val decoder = codec.get(msg.opcode, handshake) as? MessageDecoder<Message>
                ?: run {
                    logger.warn("No decoder for packet: ${msg.opcode}")
                    return
                }

        decoder.write(ctx, msg, out)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (cause !is IOException) {
            logger.debug("Exception: ", cause)
        }
        ctx.close()
    }
}