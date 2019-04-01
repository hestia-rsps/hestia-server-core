package world.gregs.hestia.core.network

import io.netty.channel.ChannelHandlerContext
import io.netty.util.AttributeKey

object NetworkConstants {

    const val CLIENT_MAJOR_VERSION = 667

    const val BASE_PORT = 43594

    val SESSION_KEY: AttributeKey<Session> = AttributeKey.valueOf<Session>("session.key")

    const val LOCALHOST = "127.0.0.1"//Don't change.
}

@Throws(IllegalStateException::class)
fun ChannelHandlerContext.getSession(): Session {
    return this.channel().attr(NetworkConstants.SESSION_KEY).get() ?: throw IllegalStateException("Session is null")
}