package ir.amirhesambandegan.easify_storage

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.OutputStream

/**
 * Saves a given Bitmap image to the device's public gallery (MediaStore) using Scoped Storage.
 * 
 * @param bitmap The [Bitmap] image to be saved.
 * @param filename The display name of the image file (including extension).
 * @param mimeType The MIME type of the image, defaulting to "image/jpeg".
 * @return True if the image was successfully saved to the gallery, false otherwise.
 */
fun Context.saveImageToGallery(bitmap: Bitmap, filename: String, mimeType: String = "image/jpeg"): Boolean {
    val resolver = contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Easify")
        }
    }
    
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    return if (uri != null) {
        resolver.openOutputStream(uri)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        true
    } else {
        false
    }
}
