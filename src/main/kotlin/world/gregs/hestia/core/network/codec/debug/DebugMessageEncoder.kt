package world.gregs.hestia.core.network.codec.debug

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.Codec
import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.codec.message.SimpleMessageEncoder

@ChannelHandler.Sharable
open class DebugMessageEncoder(codec: Codec) : SimpleMessageEncoder(codec) {

    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf) {
        logger.debug("Encoding $msg")
        super.encode(ctx, msg, out)
    }

}