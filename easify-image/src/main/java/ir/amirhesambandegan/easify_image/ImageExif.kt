package ir.amirhesambandegan.easify_image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri

/**
 * Decodes an image from a [Uri] and automatically corrects its rotation based on EXIF orientation data.
 *
 * @param context The [Context] used to resolve the URI.
 * @return A correctly rotated [Bitmap], or null if decoding or rotation fails.
 */
fun Uri.toCorrectedBitmap(context: Context): Bitmap? {
    try {
        val inputStream = context.contentResolver.openInputStream(this) ?: return null
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        
        val exifStream = context.contentResolver.openInputStream(this) ?: return bitmap
        val exif = ExifInterface(exifStream)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        exifStream.close()
        
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.postRotate(180f)
                matrix.preScale(-1f, 1f)
            }
            else -> return bitmap
        }
        
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}
