package world.gregs.hestia.core.network.codec

import world.gregs.hestia.core.network.codec.message.Message
import world.gregs.hestia.core.network.codec.message.MessageDecoder
import world.gregs.hestia.core.network.codec.message.MessageEncoder
import kotlin.reflect.KClass

/**
 * Implementation of [Codec]
 * Stores [MessageDecoder] and [MessageEncoder]'s
 */
open class MessageCodec : Codec {

    private val decoders = arrayOfNulls<MessageDecoder<*>>(256)
    private val encoders = HashMap<KClass<*>, MessageEncoder<*>>()

    override fun get(opcode: Int): MessageDecoder<*>? {
        return decoders[opcode]
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Message> get(clazz: KClass<T>): MessageEncoder<T>? {
        return encoders[clazz] as? MessageEncoder<T>
    }

    override fun bind(decoder: MessageDecoder<*>) {
        if(decoders.contains(decoder)) {
            throw IllegalArgumentException("Cannot have duplicate decoders $decoder")
        }
        decoder.opcodes.forEach { opcode ->
            if(decoders[opcode] != null) {
                throw IllegalArgumentException("Cannot have duplicate decoders $decoder $opcode")
            }
            decoders[opcode] = decoder
        }
    }

    override fun <T : Message> bind(type: KClass<T>, encoder: MessageEncoder<T>) {
        if(encoders.contains(type)) {
            throw IllegalArgumentException("Cannot have duplicate encoders $type $encoder")
        }
        encoders[type] = encoder
    }

    inline fun <reified T : Message> bind(encoder: MessageEncoder<T>) {
        bind(T::class, encoder)
    }

    override fun encoders(): HashMap<KClass<*>, MessageEncoder<*>> {
        return encoders
    }

    override fun decoders(): Array<MessageDecoder<*>?> {
        return decoders
    }
}