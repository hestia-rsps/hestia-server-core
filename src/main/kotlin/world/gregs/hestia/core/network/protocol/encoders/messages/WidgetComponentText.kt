package world.gregs.hestia.core.network.protocol.encoders.messages

import world.gregs.hestia.core.network.codec.message.Message

/**
 * Update the text of a widget component
 * @param hash The widget parent and component hash
 * @param text The text to send
 */
data class WidgetComponentText(val hash: Int, val text: String) : Message {

    /**
     * Update the text of a widget component
     * @param id The id of the parent widget
     * @param component The index of the component
     * @param text The text to send
     */
    constructor(id: Int, component: Int, text: String) : this(id shl 16 or component, text)
}