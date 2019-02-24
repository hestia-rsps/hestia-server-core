package world.gregs.hestia.core.cache.crypto

import world.gregs.hestia.core.network.NetworkConstants
import java.math.BigInteger

object Rsa {

    /**
     * Decrypt rsa bytes received from client
     */
    fun decrypt(data: ByteArray): ByteArray {
        return BigInteger(data).modPow(NetworkConstants.RSA_EXPONENT, NetworkConstants.RSA_MODULUS).toByteArray()
    }
}