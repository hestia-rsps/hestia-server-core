package world.gregs.hestia.core.network.codec

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.packets.Packet

@ChannelHandler.Sharable
class DebugEncoder : MessageToMessageEncoder<Packet>() {

    private val logger = LoggerFactory.getLogger(DebugEncoder::class.java)

    override fun encode(context: ChannelHandlerContext?, packet: Packet?, out: MutableList<Any>?) {
        if (packet == null) {
            logger.info("Null packet encode")
            return
        }

        logger.info("Encode ${packet.opcode} ${packet.type} ${packet.buffer.readerIndex()} - ${packet.buffer.writerIndex()} ${packet.buffer.array().toList()}")
        out?.add(Encoder.encode(packet))
    }
}