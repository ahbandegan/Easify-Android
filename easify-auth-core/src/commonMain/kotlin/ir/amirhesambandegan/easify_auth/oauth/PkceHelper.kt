package ir.amirhesambandegan.easify_auth.oauth

internal expect fun secureRandomBytes(size: Int): ByteArray

/**
 * Utility for Proof Key for Code Exchange (PKCE, RFC 7636) and OAuth CSRF state management.
 * Pure Kotlin Multiplatform implementation with zero Android/JVM dependencies in `commonMain`.
 */
object PkceHelper {

    private val BASE64_URL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray()

    private val SHA256_K = intArrayOf(
        0x428a2f98, 0x71374491, 0xb5c0fbcf.toInt(), 0xe9b5dba5.toInt(), 0x3956c25b, 0x59f111f1, 0x923f82a4.toInt(), 0xab1c5ed5.toInt(),
        0xd807aa98.toInt(), 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe.toInt(), 0x9bdc06a7.toInt(), 0xc19bf174.toInt(),
        0xe49b69c1.toInt(), 0xefbe4786.toInt(), 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
        0x983e5152.toInt(), 0xa831c66d.toInt(), 0xb00327c8.toInt(), 0xbf597fc7.toInt(), 0xc6e00bf3.toInt(), 0xd5a79147.toInt(), 0x06ca6351, 0x14292967,
        0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e.toInt(), 0x92722c85.toInt(),
        0xa2bfe8a1.toInt(), 0xa81a664b.toInt(), 0xc24b8b70.toInt(), 0xc76c51a3.toInt(), 0xd192e819.toInt(), 0xd6990624.toInt(), 0xf40e3585.toInt(), 0x106aa070,
        0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
        0x748f82ee, 0x78a5636f, 0x84c87814.toInt(), 0x8cc70208.toInt(), 0x90befffa.toInt(), 0xa4506ceb.toInt(), 0xbef9a3f7.toInt(), 0xc67178f2.toInt()
    )

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

    internal fun sha256(input: ByteArray): ByteArray {
        val h = intArrayOf(
            0x6a09e667, 0xbb67ae85.toInt(), 0x3c6ef372, 0xa54ff53a.toInt(),
            0x510e527f, 0x9b05688c.toInt(), 0x1f83d9ab, 0x5be0cd19
        )

        val totalLen = input.size + 1
        val padZeros = (56 - (totalLen % 64) + 64) % 64
        val padded = ByteArray(totalLen + padZeros + 8)
        input.copyInto(padded)
        padded[input.size] = 0x80.toByte()

        val bitLen = input.size.toLong() * 8L
        for (i in 0 until 8) {
            padded[padded.size - 1 - i] = ((bitLen ushr (i * 8)) and 0xFFL).toByte()
        }

        val w = IntArray(64)
        var offset = 0
        while (offset < padded.size) {
            for (i in 0 until 16) {
                val idx = offset + i * 4
                w[i] = ((padded[idx].toInt() and 0xFF) shl 24) or
                    ((padded[idx + 1].toInt() and 0xFF) shl 16) or
                    ((padded[idx + 2].toInt() and 0xFF) shl 8) or
                    (padded[idx + 3].toInt() and 0xFF)
            }
            for (i in 16 until 64) {
                val s0 = w[i - 15].rotateRight(7) xor w[i - 15].rotateRight(18) xor (w[i - 15] ushr 3)
                val s1 = w[i - 2].rotateRight(17) xor w[i - 2].rotateRight(19) xor (w[i - 2] ushr 10)
                w[i] = w[i - 16] + s0 + w[i - 7] + s1
            }

            var a = h[0]
            var b = h[1]
            var c = h[2]
            var d = h[3]
            var e = h[4]
            var f = h[5]
            var g = h[6]
            var hh = h[7]

            for (i in 0 until 64) {
                val bigS1 = e.rotateRight(6) xor e.rotateRight(11) xor e.rotateRight(25)
                val ch = (e and f) xor (e.inv() and g)
                val temp1 = hh + bigS1 + ch + SHA256_K[i] + w[i]
                val bigS0 = a.rotateRight(2) xor a.rotateRight(13) xor a.rotateRight(22)
                val maj = (a and b) xor (a and c) xor (b and c)
                val temp2 = bigS0 + maj

                hh = g
                g = f
                f = e
                e = d + temp1
                d = c
                c = b
                b = a
                a = temp1 + temp2
            }

            h[0] += a
            h[1] += b
            h[2] += c
            h[3] += d
            h[4] += e
            h[5] += f
            h[6] += g
            h[7] += hh
            offset += 64
        }

        val out = ByteArray(32)
        for (i in 0 until 8) {
            out[i * 4] = (h[i] ushr 24).toByte()
            out[i * 4 + 1] = (h[i] ushr 16).toByte()
            out[i * 4 + 2] = (h[i] ushr 8).toByte()
            out[i * 4 + 3] = h[i].toByte()
        }
        return out
    }

    /** Generates a cryptographically secure random PKCE `code_verifier`. */
    fun generateCodeVerifier(byteLength: Int = 32): String {
        val bytes = secureRandomBytes(byteLength.coerceAtLeast(32))
        return base64UrlEncode(bytes)
    }

    /** Derives the SHA-256 `code_challenge` (S256) from [codeVerifier] per RFC 7636. */
    fun generateCodeChallenge(codeVerifier: String): String {
        val ascii = codeVerifier.encodeToByteArray()
        return base64UrlEncode(sha256(ascii))
    }

    /** Generates a cryptographically secure random OAuth `state` parameter for CSRF protection. */
    fun generateState(byteLength: Int = 16): String {
        val bytes = secureRandomBytes(byteLength.coerceAtLeast(8))
        return base64UrlEncode(bytes)
    }

    /** Constant-time OAuth CSRF `state` verification helper. */
    fun verifyState(expectedState: String?, returnedState: String?): Boolean {
        if (expectedState.isNullOrEmpty() || returnedState.isNullOrEmpty()) return false
        if (expectedState.length != returnedState.length) return false
        var diff = 0
        for (i in expectedState.indices) {
            diff = diff or (expectedState[i].code xor returnedState[i].code)
        }
        return diff == 0
    }
}
