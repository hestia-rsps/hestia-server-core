package world.gregs.hestia.core.network.packets

import world.gregs.hestia.core.network.Session

/**
 * Annotation for packet opcode(s)
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class PacketOpcode(vararg val opcodes: Int)

/**
 * Annotation for packet size
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class PacketSize(val size: Int)

enum class PacketCheckType {
    REGULAR,
    ACTION,
    REQ_LOAD
}

@FunctionalInterface
interface InboundPacket {

    /**
     * Handles the packet that has just been received.
     *
     * @param session The connected session.
     * @param packet The packet that has been received.
     * @param length Length of the packet received.
     */
    fun read(session: Session, packet: Packet, length: Int): Boolean

    /**
     * Returns the check type for the packet
     */
    fun checkType(): PacketCheckType {
        return PacketCheckType.REGULAR
    }
}