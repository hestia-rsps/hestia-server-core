package world.gregs.hestia.core.network.codec

import world.gregs.hestia.core.network.codec.message.MessageDecoder

/**
 * Implementation of HandshakeCodec
 * Note: Handshake only applies to decoders
 */
open class MessageHandshakeCodec :  MessageCodec(), HandshakeCodec {
    private val handshakeCodec = MessageCodec()

    override fun get(opcode: Int, handshake: Boolean): MessageDecoder<*>? {
        return if(handshake) super.get(opcode) else handshakeCodec.get(opcode)
    }

    override fun bind(decoder: MessageDecoder<*>, handshake: Boolean) {
        if(handshake) handshakeCodec.bind(decoder) else super.bind(decoder)
    }

    override fun handshakes(): Array<MessageDecoder<*>?> {
        return handshakeCodec.decoders()
    }
}