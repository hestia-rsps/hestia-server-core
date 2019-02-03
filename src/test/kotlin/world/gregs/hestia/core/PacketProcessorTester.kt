package world.gregs.hestia.core

/*
internal class PacketProcessorTester : PacketProcessor<InboundPacket> {
    override val packets = PacketMap<InboundPacket>()
    override val open: Boolean = true
    override val logger = LoggerFactory.getLogger(PacketProcessorTester::class.java)!!

    override fun process(session: Session, handler: InboundPacket, packet: Packet, length: Int) {
        println("Process ${packet.opcode}")
    }

    @Test
    fun test() {
        packets[2] = Pair(mock(), -2)
        packets[32] = Pair(mock(), -1)
        packets[23] = Pair(mock(), 1)
        packets[36] = Pair(mock(), -1)
        val channel = mock<Channel>()
        whenever(channel.isOpen).thenReturn(true)
        val session = Session(channel)
        val buffer = Unpooled.buffer(9)
        //2, 0, 8, 0, 0, 0, 0, 57, 0, 1, 0
        //87, 1, 2, -3, 1, -9, 0, 16
        buffer.writeBytes(byteArrayOf(2, 0, 18, 0, 0, 0, 0, 85, 0, 11, 1, 84, 101, 115, 116, 0, 0, 0, 0, 0, 0))
        process(session, Packet(buffer = buffer))
    }
}*/
