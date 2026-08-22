package ir.amirhesambandegan.easify_image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

/**
 * Compresses the [Bitmap] to a specific target size in kilobytes.
 * Uses a binary search approach to find the best quality that fits within the target size.
 *
 * @param maxSizeKB The maximum allowed size of the compressed image in kilobytes.
 * @return A [ByteArray] containing the compressed image data.
 */
fun Bitmap.compressToTargetSize(maxSizeKB: Int): ByteArray {
    var minQuality = 0
    var maxQuality = 100
    var bestBytes = ByteArrayOutputStream().apply { compress(Bitmap.CompressFormat.JPEG, maxQuality, this) }.toByteArray()
    
    if (bestBytes.size / 1024 <= maxSizeKB) return bestBytes

    var currentQuality: Int
    while (minQuality <= maxQuality) {
        currentQuality = (minQuality + maxQuality) / 2
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, currentQuality, stream)
        val bytes = stream.toByteArray()
        val sizeKB = bytes.size / 1024
        
        if (sizeKB <= maxSizeKB) {
            bestBytes = bytes
            minQuality = currentQuality + 1 // try to get better quality
        } else {
            maxQuality = currentQuality - 1
        }
    }
    return bestBytes
}

/**
 * Compresses the [Bitmap] to a specific target size in kilobytes and returns the result as a new [Bitmap].
 *
 * @param maxSizeKB The maximum allowed size of the compressed image in kilobytes.
 * @return A new [Bitmap] representing the compressed image.
 */
fun Bitmap.compressToTargetBitmap(maxSizeKB: Int): Bitmap {
    val bytes = this.compressToTargetSize(maxSizeKB)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}
