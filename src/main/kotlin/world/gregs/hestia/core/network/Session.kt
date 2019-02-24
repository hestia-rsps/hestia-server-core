package world.gregs.hestia.core.network

import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.client.Response
import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.protocol.encoders.messages.ClientResponseCode
import java.net.InetSocketAddress

data class Session(val channel: Channel? = null) {

    var ping = System.currentTimeMillis()
    var id = -1

    fun write(message: Message, priority: Boolean = false, close: Boolean = false) {
        if(channel?.isActive == true) {
            synchronized(channel) {
                val future = if(priority) channel.writeAndFlush(message) else channel.write(message)
                if(close) {
                    future.addListener(ChannelFutureListener.CLOSE) ?: channel.close()
                }
            }
        } else {
            logger.info("Channel closed: $message $channel $id")
        }
    }

    fun flush() {
        channel?.flush()
    }

    fun close() {
        channel?.close()
    }

    fun getHost(): String {
        return (channel?.remoteAddress() as? InetSocketAddress)?.address?.hostAddress ?: NetworkConstants.LOCALHOST
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Session::class.java)!!
    }
}

fun ChannelHandlerContext.clientRespond(response: Response) {
    channel().writeAndFlush(ClientResponseCode(response.opcode))
}