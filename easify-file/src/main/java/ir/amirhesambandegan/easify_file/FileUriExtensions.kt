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
        runCatching {
            context.contentResolver.query(this, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index != -1 && !cursor.isNull(index)) {
                        result = cursor.getString(index)
                    }
                }
            }
        }
    }
    if (result.isNullOrBlank()) {
        result = path?.let { File(it).name }
    }
    val sanitized = result?.let { File(it).name }?.takeIf { it.isNotBlank() && it != "." && it != ".." }
    return sanitized ?: "unknown_file"
}

fun Uri.getFileSize(context: Context): Long {
    var result = 0L
    if (scheme == "content") {
        runCatching {
            context.contentResolver.query(this, arrayOf(OpenableColumns.SIZE), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (index != -1 && !cursor.isNull(index)) {
                        result = cursor.getLong(index)
                    }
                }
            }
        }
    }
    if (result == 0L && scheme == "file") {
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
    return try {
        val safeName = File(getFileName(context)).name
            .replace(Regex("[^A-Za-z0-9._-]"), "_")
            .ifBlank { "temp_${System.currentTimeMillis()}" }

        val tempDir = File(context.applicationContext.cacheDir, "easify_temp").apply { mkdirs() }.canonicalFile
        val tempFile = File(tempDir, safeName).canonicalFile
        if (!tempFile.path.startsWith(tempDir.path + File.separator)) return null

        val input = context.contentResolver.openInputStream(this) ?: return null
        input.use { stream ->
            FileOutputStream(tempFile).use { output ->
                stream.copyTo(output)
            }
        }
        tempFile
    } catch (_: Exception) {
        null
    }
}

fun Context.clearEasifyCache() {
    val tempDir = File(applicationContext.cacheDir, "easify_temp")
    if (tempDir.exists()) {
        tempDir.deleteRecursively()
    }
}
