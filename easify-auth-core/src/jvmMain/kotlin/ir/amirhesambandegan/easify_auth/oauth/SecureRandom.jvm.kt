package ir.amirhesambandegan.easify_auth.oauth

import java.security.SecureRandom

private object JvmSecureRandomHolder {
    val random = SecureRandom()
}

internal actual fun secureRandomBytes(size: Int): ByteArray =
    ByteArray(size).also { JvmSecureRandomHolder.random.nextBytes(it) }
