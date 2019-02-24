package world.gregs.hestia.core.network.codec.inbound

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class InboundHandlerTest {
//    private lateinit var handler: InboundHandler
    private lateinit var buffer: ByteBuf

    @BeforeEach
    fun setup() {
//        handler = mock()
        buffer = Unpooled.buffer()
//        whenever(handler.channelRead(any(), any())).thenCallRealMethod()
    }

    @Test
    fun `Handles normal buffer`() {
        //Given
        buffer.writeInt(1)
        //When
        read()
        //Then
        assertHandled(1)
    }

    @Test
    fun `Empty buffer not handled`() {
        //When
        read()
        //Then
        assertHandled(0)
    }

    @Test
    fun `Giant buffer not handled`() {
        //Given
        for(i in 0 .. 7500) {
            buffer.writeByte(0)
        }
        //When
        read()
        //Then
        assertHandled(0)
    }

    private fun read() {
//        handler.channelRead(mock(), buffer)
    }
    private fun assertHandled(times: Int) {
//        verify(handler, times(times)).handle(any(), any())
    }
    /*
    Test list:
    Too small
    Too big
    Fragmented packets
    Writer index = reader index = end
     */
}