package ir.amirhesambandegan.easify_image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * Utility functions for image processing and conversion.
 */
object ImageUtils {

    /**
     * Safely decodes a [Uri] into a [Bitmap].
     *
     * @param context The [Context] used to access the content resolver.
     * @return The decoded [Bitmap], or null if an error occurs.
     */
    fun Uri.toBitmap(context: Context): Bitmap? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(this)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Compresses the [Bitmap] and returns it as a [ByteArray].
     *
     * @param quality Hint to the compressor, 0-100. 0 meaning compress for small size, 100 meaning compress for max quality.
     * @param format The format of the compressed image. Defaults to [Bitmap.CompressFormat.JPEG].
     * @return A [ByteArray] containing the compressed image data.
     */
    fun Bitmap.compressToBytes(
        quality: Int = 80,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(format, quality, stream)
        return stream.toByteArray()
    }

    /**
     * Converts a [Bitmap] to a Base64 encoded string.
     *
     * @param quality Hint to the compressor, 0-100. Defaults to 80.
     * @param format The format of the compressed image. Defaults to [Bitmap.CompressFormat.JPEG].
     * @return The Base64 encoded string representation of the bitmap.
     */
    fun Bitmap.toBase64(
        quality: Int = 80,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): String {
        val bytes = this.compressToBytes(quality, format)
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    /**
     * Resizes the [Bitmap] while maintaining its aspect ratio.
     *
     * @param maxWidth The maximum allowed width in pixels.
     * @param maxHeight The maximum allowed height in pixels.
     * @return A new resized [Bitmap].
     */
    fun Bitmap.resize(maxWidth: Int, maxHeight: Int): Bitmap {
        val width = this.width
        val height = this.height
        
        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

        var finalWidth = maxWidth
        var finalHeight = maxHeight
        
        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }

        return Bitmap.createScaledBitmap(this, finalWidth, finalHeight, true)
    }
}
