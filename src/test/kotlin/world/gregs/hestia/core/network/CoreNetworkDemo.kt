package world.gregs.hestia.core.network

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.codec.Pipeline
import world.gregs.hestia.core.network.codec.decode.HandshakeDecoder
import world.gregs.hestia.core.network.server.Network

internal class CoreNetworkDemo {
    internal class LoginHandshakeDecoder : HandshakeDecoder() {


        override fun decode(ctx: ChannelHandlerContext?, `in`: ByteBuf?, out: MutableList<Any>?) {
            println("Decode ${`in`?.readableBytes()}")
            super.decode(ctx, `in`, out)
        }

        override fun process(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
            println("Process")
            super.process(ctx, buf, out)
            println("Complete: ${out.size} ${out.firstOrNull()}")
        }

        override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
            println("Read $msg")
            super.channelRead(ctx, msg)
        }

        override val logger = LoggerFactory.getLogger(this::class.java)!!

        override fun getSize(opcode: Int): Int? {
            println("Size $opcode")
            return if(opcode == 14) 0 else null
        }

    }

    @Test
    fun `Test inbound`() {
        val pipe = Pipeline()
        pipe.add(LoginHandshakeDecoder::class)
        pipe.add(@ChannelHandler.Sharable object : ChannelInboundHandlerAdapter() {
            override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
                println("Actual Read $msg")
            }
        })
        Network(name = "Login Server", channel = pipe).start(NetworkConstants.BASE_PORT)

        while (true) {

        }
    }
}