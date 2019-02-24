package world.gregs.hestia.core.network.protocol.messages

import world.gregs.hestia.core.network.client.Response
import world.gregs.hestia.core.network.codec.message.Message

/**
 * Login response from the social server
 * @param session The players session id
 * @param response The response code [Response]
 */
data class PlayerLoginResponse(val session: Int, val response: Int): Message {
    constructor(session: Int, response: Response) : this(session, response.opcode)
}