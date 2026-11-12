package ir.amirhesambandegan.easify_storage

import java.io.File
import java.security.MessageDigest
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

/**
 * Provides file size formatting and streaming cryptographic checksum calculation.
 */
object FileAnalyzer {

    private val SIZE_UNITS = arrayOf("B", "KB", "MB", "GB", "TB", "PB", "EB")

    /**
     * Formats [sizeInBytes] into a locale-stable human-readable representation (e.g., `"1.50 MB"`).
     */
    fun formatFileSize(sizeInBytes: Long): String {
        if (sizeInBytes <= 0L) return "0 B"
        val groups = (log10(sizeInBytes.toDouble()) / log10(1024.0))
            .toInt()
            .coerceIn(0, SIZE_UNITS.lastIndex)
        return String.format(
            Locale.US,
            "%.2f %s",
            sizeInBytes / 1024.0.pow(groups.toDouble()),
            SIZE_UNITS[groups]
        )
    }

    /**
     * Computes the lowercase hexadecimal digest of [file] using [algorithm] (defaults to `"SHA-256"`).
     */
    fun getFileChecksum(file: File, algorithm: String = "SHA-256"): String? {
        if (!file.exists() || !file.isFile) return null
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
        } catch (_: Exception) {
            null
        }
    }
}
