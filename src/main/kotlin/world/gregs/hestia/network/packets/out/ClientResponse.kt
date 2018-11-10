package world.gregs.hestia.network.packets.out

import world.gregs.hestia.network.packets.Packet

class ClientResponse(response: Response): Packet.Builder() {

    init {
        writeByte(response.opcode)
    }

}