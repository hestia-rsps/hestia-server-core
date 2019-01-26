package world.gregs.hestia.core.network.codec.inbound

import io.netty.buffer.ByteBuf
import world.gregs.hestia.core.network.Session

abstract class HandshakeHandler : SessionInboundHandler() {

    override fun handle(session: Session, buffer: ByteBuf) {
        if (!session.handshake) {
            session.handshake = handshake(session, buffer)
        } else {
            process(session, buffer)
        }
    }

    abstract fun handshake(session: Session, buffer: ByteBuf): Boolean

    abstract fun process(session: Session, buffer: ByteBuf)
}