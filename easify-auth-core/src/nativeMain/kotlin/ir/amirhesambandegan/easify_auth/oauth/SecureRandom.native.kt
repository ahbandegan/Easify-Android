@file:OptIn(kotlin.uuid.ExperimentalUuidApi::class)

package ir.amirhesambandegan.easify_auth.oauth

import kotlin.uuid.Uuid

internal actual fun secureRandomBytes(size: Int): ByteArray {
    val result = ByteArray(size)
    var pos = 0
    val unbiasedIndices = intArrayOf(0, 1, 2, 3, 4, 5, 7, 9, 10, 11, 12, 13, 14, 15)
    while (pos < size) {
        val uuidBytes = Uuid.random().toByteArray()
        for (idx in unbiasedIndices) {
            if (pos >= size) break
            result[pos++] = uuidBytes[idx]
        }
    }
    return result
}
