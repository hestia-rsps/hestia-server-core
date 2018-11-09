package world.gregs.network.packets.out

import world.gregs.network.packets.Packet

class ClientResponse(response: Response): Packet.Builder() {

    init {
        writeByte(response.opcode)
    }

}