package ir.amirhesambandegan.easify_file

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream

fun Bitmap.saveToGallery(
    context: Context,
    fileName: String,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): Boolean {
    val safeName = File(fileName).name.ifBlank { "image_${System.currentTimeMillis()}.jpg" }
    val safeQuality = quality.coerceIn(0, 100)
    val mimeType = when (compressFormat) {
        Bitmap.CompressFormat.PNG -> "image/png"
        else -> "image/jpeg"
    }

    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, safeName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/Easify")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }

    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) ?: return false
    return try {
        val compressed = resolver.openOutputStream(uri)?.use { output ->
            compress(compressFormat, safeQuality, output)
        } ?: false

        if (!compressed) {
            runCatching { resolver.delete(uri, null, null) }
            return false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        }
        true
    } catch (_: Exception) {
        runCatching { resolver.delete(uri, null, null) }
        false
    }
}

fun File.saveToDownloads(context: Context, fileName: String, mimeType: String = "*/*"): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return false
    val safeName = File(fileName).name.ifBlank { name.ifBlank { "download_${System.currentTimeMillis()}" } }
    val resolver = context.contentResolver

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, safeName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/Easify")
        put(MediaStore.MediaColumns.IS_PENDING, 1)
    }

    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues) ?: return false
    return try {
        val wrote = resolver.openOutputStream(uri)?.use { output ->
            FileInputStream(this).use { input ->
                input.copyTo(output)
            }
            true
        } ?: false

        if (!wrote) {
            runCatching { resolver.delete(uri, null, null) }
            return false
        }

        contentValues.clear()
        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
        resolver.update(uri, contentValues, null, null)
        true
    } catch (_: Exception) {
        runCatching { resolver.delete(uri, null, null) }
        false
    }
}
