package ir.amirhesambandegan.easify_image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Utility functions for Android [Bitmap] processing and conversion.
 */
object ImageUtils {

    fun Uri.toBitmap(context: Context): Bitmap? {
        return runCatching {
            context.contentResolver.openInputStream(this)?.use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        }.getOrNull()
    }

    fun Bitmap.compressToBytes(
        quality: Int = 80,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): ByteArray {
        val safeQuality = quality.coerceIn(0, 100)
        val stream = ByteArrayOutputStream()
        this.compress(format, safeQuality, stream)
        return stream.toByteArray()
    }

    fun Bitmap.toBase64(
        quality: Int = 80,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): String {
        val bytes = this.compressToBytes(quality, format)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun Bitmap.resize(maxWidth: Int, maxHeight: Int): Bitmap {
        if (width <= 0 || height <= 0 || maxWidth <= 0 || maxHeight <= 0) return this

        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

        var finalWidth = maxWidth
        var finalHeight = maxHeight

        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt().coerceAtLeast(1)
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt().coerceAtLeast(1)
        }

        return Bitmap.createScaledBitmap(this, finalWidth.coerceAtLeast(1), finalHeight.coerceAtLeast(1), true)
    }
}
