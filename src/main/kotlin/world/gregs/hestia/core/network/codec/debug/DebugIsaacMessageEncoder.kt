package world.gregs.hestia.core.network.codec.debug

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.cache.crypto.Cipher
import world.gregs.hestia.core.network.codec.Codec
import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.codec.message.SimpleIsaacMessageEncoder

open class DebugIsaacMessageEncoder(codec: Codec, cipher: Cipher) : SimpleIsaacMessageEncoder(codec, cipher) {

    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf) {
        logger.debug("Encoding $msg $cipher")
        super.encode(ctx, msg, out)
    }

}