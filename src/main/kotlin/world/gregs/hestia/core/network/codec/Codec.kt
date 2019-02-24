package world.gregs.hestia.core.network.codec

import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.codec.message.MessageDecoder
import world.gregs.hestia.core.network.codec.message.MessageEncoder
import kotlin.reflect.KClass

/**
 * Storage and retrieval of decoders & encoders
 */
interface Codec {

    /**
     * Retrieving a decoder by id
     * @param opcode The decoder opcode
     * @return The decoder if found
     */
    fun get(opcode: Int): MessageDecoder<*>?

    /**
     * Retrieving a encoder by type
     * @param clazz The [Message] class type
     * @return The encoder if found
     */
    fun <T : Message> get(clazz: KClass<T>): MessageEncoder<T>?

    /**
     * Binds a decoder to the codec
     * @param decoder The decoder to add
     */
    fun bind(decoder: MessageDecoder<*>)

    /**
     * Binds an encoder by type to the codec
     * @param type The [Message] class
     * @param encoder The encoder
     */
    fun <T : Message> bind(type: KClass<T>, encoder: MessageEncoder<T>)

    /**
     * Returns complete list of encoders
     * @return encoders
     */
    fun encoders(): HashMap<KClass<*>, MessageEncoder<*>>

    /**
     * Returns complete list of decoders
     * @return decoders
     */
    fun decoders(): Array<MessageDecoder<*>?>

}