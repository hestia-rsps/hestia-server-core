package world.gregs.hestia.core.network.protocol.decoders

import io.netty.channel.ChannelHandlerContext
import world.gregs.hestia.core.network.codec.message.MessageDecoder
import world.gregs.hestia.core.network.codec.packet.Packet
import world.gregs.hestia.core.network.protocol.WorldOpcodes.FRIENDS_CHAT_SETUP
import world.gregs.hestia.core.network.protocol.messages.FriendsChatSetupRequest

class FriendsChatSetupRequestDecoder : MessageDecoder<FriendsChatSetupRequest>(4, FRIENDS_CHAT_SETUP) {

    override fun decode(ctx: ChannelHandlerContext, packet: Packet): FriendsChatSetupRequest? {
        return FriendsChatSetupRequest(packet.readInt())
    }

}