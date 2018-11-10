package world.gregs.hestia.network.packets

import world.gregs.hestia.services.Loader
import world.gregs.hestia.services.plural
import org.slf4j.LoggerFactory
import kotlin.system.measureNanoTime

class PacketLoader(path: String) {
    private val plugins = Loader(path)

    private val logger = LoggerFactory.getLogger(PacketLoader::class.java)

    fun load(packageName: String): PacketMap {
        val packets = PacketMap()

        val time = measureNanoTime {
            //Find all InboundPacket classes
            val classes = plugins.load(packageName, InboundPacket::class)
            classes.forEach { packet ->
                //Get annotations
                val opcodeAnnotation = packet::class.java.getAnnotation(PacketOpcode::class.java) ?: return@forEach
                val sizeAnnotation = packet::class.java.getAnnotation(PacketSize::class.java) ?: return@forEach
                //Insert into packets list for all opcodes
                opcodeAnnotation.opcodes.forEach { opcode ->
                    packets[opcode] = Pair(packet, sizeAnnotation.size)
                }
            }
        }

        logger.debug("${packets.size} ${"packet".plural(packets.size)} loaded in ${time / 1000000}ms")
        return packets
    }

}