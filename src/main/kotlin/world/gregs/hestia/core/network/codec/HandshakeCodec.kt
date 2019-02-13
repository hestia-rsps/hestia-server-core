package world.gregs.hestia.core.network.codec

import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.codec.message.MessageDecoder
import world.gregs.hestia.core.network.codec.message.MessageEncoder
import kotlin.reflect.KClass

/**
 * Wrapper for two codecs to be switched once handshake is complete
 */
interface HandshakeCodec {

    /**
     * Retrieving a decoder by id
     * @param opcode The decoder opcode
     * @param handshake Whether the handshake is complete
     * @return The decoder if found
     */
    fun get(opcode: Int, handshake: Boolean): MessageDecoder<*>?

    /**
     * Retrieving a encoder by type
     * @param clazz The [Message] class type
     * @return The encoder if found
     */
    fun <T : Message> get(clazz: KClass<T>): MessageEncoder<T>?

    /**
     * Binds a decoder to the codec
     * @param decoder The decoder to add
     * @param handshake Whether to bind to handshake codec
     */
    fun bind(decoder: MessageDecoder<*>, handshake: Boolean = false)

    /**
     * Binds an encoder by type to the codec
     * @param type The [Message] class
     * @param encoder The encoder
     */
    fun <T : Message> bind(type: KClass<T>, encoder: MessageEncoder<T>)

    /**
     * Returns complete list of handshake decoders
     * @return handshake decoders
     */
    fun handshakes(): Array<MessageDecoder<*>?>

}