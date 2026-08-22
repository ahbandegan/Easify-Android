package ir.amirhesambandegan.easify_file

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream

fun Uri.getFileName(context: Context): String {
    var result: String? = null
    if (scheme == "content") {
        context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    result = cursor.getString(index)
                }
            }
        }
    }
    if (result == null) {
        result = path?.let { File(it).name }
    }
    return result ?: "unknown_file"
}

fun Uri.getFileSize(context: Context): Long {
    var result: Long = 0
    if (scheme == "content") {
        context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (index != -1) {
                    result = cursor.getLong(index)
                }
            }
        }
    }
    if (result == 0L) {
        result = path?.let { File(it).length() } ?: 0L
    }
    return result
}

fun Uri.getMimeType(context: Context): String {
    return if (scheme == "content") {
        context.contentResolver.getType(this) ?: "*/*"
    } else {
        val extension = MimeTypeMap.getFileExtensionFromUrl(toString())
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension?.lowercase()) ?: "*/*"
    }
}

fun Uri.toTempFile(context: Context): File? {
    try {
        val fileName = getFileName(context)
        val tempDir = File(context.cacheDir, "easify_temp")
        if (!tempDir.exists()) tempDir.mkdirs()
        
        val tempFile = File(tempDir, fileName)
        context.contentResolver.openInputStream(this)?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun Context.clearEasifyCache() {
    val tempDir = File(cacheDir, "easify_temp")
    if (tempDir.exists()) {
        tempDir.deleteRecursively()
    }
}
