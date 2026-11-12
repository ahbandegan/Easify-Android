package ir.amirhesambandegan.easify_auth.oauth

import java.security.SecureRandom

private object AndroidSecureRandomHolder {
    val random = SecureRandom()
}

internal actual fun secureRandomBytes(size: Int): ByteArray =
    ByteArray(size).also { AndroidSecureRandomHolder.random.nextBytes(it) }
