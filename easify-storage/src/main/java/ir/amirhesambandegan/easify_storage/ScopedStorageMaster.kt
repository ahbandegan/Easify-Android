package ir.amirhesambandegan.easify_storage

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File

/**
 * Saves a [Bitmap] image to the device's public gallery via MediaStore Scoped Storage.
 * Sanitizes [filename], marks `IS_PENDING` on Android 10+ during writes, verifies compression,
 * and cleans up orphaned MediaStore rows on failure.
 */
fun Context.saveImageToGallery(
    bitmap: Bitmap,
    filename: String,
    mimeType: String = "image/jpeg",
    quality: Int = 95
): Boolean {
    val safeFilename = File(filename).name.ifBlank { "image_${System.currentTimeMillis()}.jpg" }
    val format = when (mimeType.lowercase()) {
        "image/png" -> Bitmap.CompressFormat.PNG
        "image/webp" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            @Suppress("DEPRECATION")
            Bitmap.CompressFormat.WEBP
        }
        else -> Bitmap.CompressFormat.JPEG
    }
    val safeQuality = quality.coerceIn(0, 100)

    val resolver = contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, safeFilename)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/Easify")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }

    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) ?: return false

    val compressed = runCatching {
        resolver.openOutputStream(uri)?.use { outputStream ->
            bitmap.compress(format, safeQuality, outputStream)
        } ?: false
    }.getOrDefault(false)

    if (!compressed) {
        runCatching { resolver.delete(uri, null, null) }
        return false
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val doneValues = ContentValues().apply {
            put(MediaStore.MediaColumns.IS_PENDING, 0)
        }
        resolver.update(uri, doneValues, null, null)
    }

    return true
}
