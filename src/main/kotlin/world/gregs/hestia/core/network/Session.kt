package world.gregs.hestia.core.network

import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.packets.Packet
import world.gregs.hestia.core.network.packets.out.ClientResponse
import world.gregs.hestia.core.network.packets.out.Response
import java.net.InetSocketAddress

data class Session(val channel: Channel? = null) {

    var ping = System.currentTimeMillis()
    var id = -1

    fun write(builder: Packet.Builder, close: Boolean = false) {
        write(builder.build(), close)
    }

    fun write(packet: Packet?, close: Boolean = false) {
        if (packet == null) {
            return
        }

        if(channel?.isOpen == true) {
            synchronized(channel) {
                val future = channel.writeAndFlush(packet)
                if(close) {
                    future.addListener(ChannelFutureListener.CLOSE) ?: channel.close()
                }
            }
        } else {
            logger.info("Channel closed: $packet $channel $id")
        }
    }

    fun respond(response: Response) {
        write(ClientResponse(response), response.opcode > 2)
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