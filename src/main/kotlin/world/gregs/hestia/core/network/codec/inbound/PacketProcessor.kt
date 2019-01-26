package world.gregs.hestia.core.network.codec.inbound

import org.slf4j.Logger
import world.gregs.hestia.core.network.Session
import world.gregs.hestia.core.network.packets.Packet
import world.gregs.hestia.core.services.load.PacketMap

interface PacketProcessor<T> {
    val packets: PacketMap<T>
    val open: Boolean
    val logger: Logger

    fun process(session: Session, packet: Packet) {
        while (packet.readableBytes() > 0 && session.channel?.isOpen == true && open) {
            val packetId = packet.readUnsignedByte()
            val handler = packets.getPacket(packetId)
            val packetSize = getSize(packetId)

            if (packetSize == null) {
                if (packetId != 16) {//Stop spam for ping packet
                    logger.warn("Unhandled packet: $packetId ${packet.readableBytes()}")
                }
                break
            }

            val startIndex = packet.buffer.readerIndex() - 1

            packet.opcode = packetId

            //Process packet size
            var length = when (packetSize) {
                -1 -> packet.readUnsignedByte()
                -2 -> packet.readShort()
                -3 -> packet.readInt()
                -4 -> {
                    val readable = packet.readableBytes()
                    logger.warn("Packet ${packet.opcode} is missing a proper size. Might be $readable")
                    readable
                }
                else -> packetSize
            }

            if (length > packet.readableBytes()) {
                logger.warn("Packet ${packet.opcode} $packetSize has too large size $length ${packet.readableBytes()}")
                length = packet.readableBytes()
            }

            session.ping = System.currentTimeMillis()

            //Mark current position
            val index = packet.buffer.readerIndex()

            //Process the packet
            process(session, handler, packet, length, startIndex)

            //Make sure we've read everything in this packet before moving on
            packet.buffer.readerIndex(index + length)
        }
    }

    fun getSize(packetId: Int): Int? {
        return packets.getSize(packetId)
    }

    fun process(session: Session, handler: T?, packet: Packet, length: Int, offset: Int) {
        if (handler == null) {
            logger.warn("Unhandled packet processing: ${packet.opcode} $length")
            return
        }

        //Process the packet
        process(session, handler, packet, length)
    }

    fun process(session: Session, handler: T, packet: Packet, length: Int)
}