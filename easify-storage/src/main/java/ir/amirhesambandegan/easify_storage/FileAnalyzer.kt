package ir.amirhesambandegan.easify_storage

import java.io.File
import java.security.MessageDigest
import kotlin.math.log10
import kotlin.math.pow

object FileAnalyzer {

    fun formatFileSize(sizeInBytes: Long): String {
        if (sizeInBytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(sizeInBytes.toDouble()) / log10(1024.0)).toInt()
        return String.format("%.2f %s", sizeInBytes / 1024.0.pow(digitGroups.toDouble()), units[digitGroups])
    }

    fun getFileChecksum(file: File, algorithm: String = "SHA-256"): String? {
        if (!file.exists()) return null
        return try {
            val digest = MessageDigest.getInstance(algorithm)
            file.inputStream().use { fis ->
                val buffer = ByteArray(8192)
                var read: Int
                while (fis.read(buffer).also { read = it } != -1) {
                    digest.update(buffer, 0, read)
                }
            }
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            null
        }
    }
}
