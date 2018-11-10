package world.gregs.hestia.core.network.packets.out

import world.gregs.hestia.core.network.packets.Packet

class ClientResponse(response: Response): Packet.Builder() {

    init {
        writeByte(response.opcode)
    }

}