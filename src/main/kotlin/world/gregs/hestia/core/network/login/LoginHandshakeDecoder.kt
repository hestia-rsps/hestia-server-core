package world.gregs.hestia.core.network.login

import org.slf4j.LoggerFactory
import world.gregs.hestia.core.network.codec.decode.HandshakeDecoder

/**
 * LoginHandshakeDecoder
 * Paired with [LoginHandshake]
 */
class LoginHandshakeDecoder : HandshakeDecoder() {

    override val logger = LoggerFactory.getLogger(this::class.java)!!

    override fun getSize(opcode: Int): Int? {
        return if(opcode == 14) 0 else null
    }

}