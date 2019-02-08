package world.gregs.hestia.core.network.packets

import world.gregs.hestia.core.network.Session

/**
 * Annotation for packet opcode(s) & size
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class PacketInfo(val size: Int, vararg val opcodes: Int)

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
     */
    fun read(session: Session, packet: Packet)

    /**
     * Returns the check type for the packet
     */
    fun checkType(): PacketCheckType {
        return PacketCheckType.REGULAR
    }
}