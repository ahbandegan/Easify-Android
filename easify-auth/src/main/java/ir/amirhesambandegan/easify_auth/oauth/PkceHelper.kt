package ir.amirhesambandegan.easify_auth.oauth

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom

/**
 * Utility for Proof Key for Code Exchange (PKCE) according to RFC 7636,
 * and secure state token generation for CSRF protection.
 */
object PkceHelper {

    private val secureRandom = SecureRandom()
    private val BASE64_URL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray()

    /**
     * URL-safe Base64 encoder without padding (RFC 4648 / RFC 7636).
     * Self-contained to allow execution in pure JVM tests without mocking android.util.Base64.
     */
    internal fun base64UrlEncode(bytes: ByteArray): String {
        val sb = StringBuilder((bytes.size * 4 + 2) / 3)
        var i = 0
        while (i < bytes.size) {
            val b0 = bytes[i++].toInt() and 0xFF
            val b1 = if (i < bytes.size) bytes[i++].toInt() and 0xFF else -1
            val b2 = if (i < bytes.size) bytes[i++].toInt() and 0xFF else -1

            val c0 = b0 ushr 2
            val c1 = ((b0 and 0x03) shl 4) or (if (b1 >= 0) (b1 ushr 4) else 0)
            sb.append(BASE64_URL_CHARS[c0])
            sb.append(BASE64_URL_CHARS[c1])

            if (b1 >= 0) {
                val c2 = ((b1 and 0x0F) shl 2) or (if (b2 >= 0) (b2 ushr 6) else 0)
                sb.append(BASE64_URL_CHARS[c2])
            }
            if (b2 >= 0) {
                val c3 = b2 and 0x3F
                sb.append(BASE64_URL_CHARS[c3])
            }
        }
        return sb.toString()
    }

    /**
     * Generates a cryptographically secure random `code_verifier` string.
     *
     * @param byteLength Number of random bytes to generate before Base64 URL encoding.
     * Default 32 bytes yields a 43-character URL-safe string.
     */
    fun generateCodeVerifier(byteLength: Int = 32): String {
        val bytes = ByteArray(byteLength)
        secureRandom.nextBytes(bytes)
        return base64UrlEncode(bytes)
    }

    /**
     * Generates a `code_challenge` derived from the given [codeVerifier] using SHA-256 (S256).
     *
     * @param codeVerifier The PKCE code verifier.
     * @return URL-safe Base64-encoded SHA-256 digest without padding.
     */
    fun generateCodeChallenge(codeVerifier: String): String {
        val bytes = codeVerifier.toByteArray(StandardCharsets.US_ASCII)
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(bytes)
        return base64UrlEncode(digest)
    }

    /**
     * Generates a cryptographically secure random state parameter for OAuth CSRF protection.
     */
    fun generateState(byteLength: Int = 16): String {
        val bytes = ByteArray(byteLength)
        secureRandom.nextBytes(bytes)
        return base64UrlEncode(bytes)
    }
}
