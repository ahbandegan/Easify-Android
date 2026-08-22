package ir.amirhesambandegan.easify_file

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream

fun Bitmap.saveToGallery(context: Context, fileName: String, compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 100): Boolean {
    return try {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, if (compressFormat == Bitmap.CompressFormat.PNG) "image/png" else "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Easify")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }
        
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) ?: return false
        
        context.contentResolver.openOutputStream(uri)?.use { output ->
            compress(compressFormat, quality, output)
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            context.contentResolver.update(uri, contentValues, null, null)
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun File.saveToDownloads(context: Context, fileName: String, mimeType: String = "*/*"): Boolean {
    return try {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/Easify")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }
        
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            return false // Fallback logic typically involves WRITE_EXTERNAL_STORAGE which we avoid for simplicity
        }
        
        if (uri != null) {
            context.contentResolver.openOutputStream(uri)?.use { output ->
                FileInputStream(this).use { input ->
                    input.copyTo(output)
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                context.contentResolver.update(uri, contentValues, null, null)
            }
            true
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
}
