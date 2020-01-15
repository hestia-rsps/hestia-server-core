package world.gregs.hestia.core.network.protocol.encoders.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Update the text of a interface component
 * @param hash The interface parent and component hash
 * @param text The text to send
 */
data class InterfaceComponentText(val hash: Int, val text: String) : Message {

    /**
     * Update the text of a interface component
     * @param id The id of the parent interface
     * @param component The index of the component
     * @param text The text to send
     */
    constructor(id: Int, component: Int, text: String) : this(id shl 16 or component, text)
}