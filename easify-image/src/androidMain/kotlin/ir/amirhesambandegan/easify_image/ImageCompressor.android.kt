package ir.amirhesambandegan.easify_image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

/**
 * Compresses the [Bitmap] using binary search to fit under [maxSizeKB] kilobytes.
 */
fun Bitmap.compressToTargetSize(maxSizeKB: Int): ByteArray {
    if (maxSizeKB <= 0) {
        return ByteArrayOutputStream().apply { compress(Bitmap.CompressFormat.JPEG, 0, this) }.toByteArray()
    }

    var minQuality = 0
    var maxQuality = 100
    var bestBytes = ByteArrayOutputStream().apply { compress(Bitmap.CompressFormat.JPEG, maxQuality, this) }.toByteArray()

    if (bestBytes.size / 1024 <= maxSizeKB) return bestBytes

    while (minQuality <= maxQuality) {
        val currentQuality = (minQuality + maxQuality) / 2
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, currentQuality, stream)
        val bytes = stream.toByteArray()
        val sizeKB = bytes.size / 1024

        if (sizeKB <= maxSizeKB) {
            bestBytes = bytes
            minQuality = currentQuality + 1
        } else {
            bestBytes = bytes
            maxQuality = currentQuality - 1
        }
    }
    return bestBytes
}

/**
 * Compresses the [Bitmap] under [maxSizeKB] and returns the decoded result [Bitmap].
 */
fun Bitmap.compressToTargetBitmap(maxSizeKB: Int): Bitmap {
    val bytes = this.compressToTargetSize(maxSizeKB)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: this
}
