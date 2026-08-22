package ir.amirhesambandegan.easify_storage

import java.io.File
import java.security.MessageDigest
import kotlin.math.log10
import kotlin.math.pow

/**
 * A utility object for analyzing files, including calculating sizes and checksums.
 */
object FileAnalyzer {

    /**
     * Formats a given file size in bytes into a human-readable string representation
     * (e.g., "1.50 MB", "500.00 KB").
     *
     * @param sizeInBytes The file size in bytes.
     * @return A formatted string representing the human-readable file size.
     */
    fun formatFileSize(sizeInBytes: Long): String {
        if (sizeInBytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(sizeInBytes.toDouble()) / log10(1024.0)).toInt()
        return String.format("%.2f %s", sizeInBytes / 1024.0.pow(digitGroups.toDouble()), units[digitGroups])
    }

    /**
     * Calculates the cryptographic checksum of a given file.
     *
     * @param file The file to calculate the checksum for.
     * @param algorithm The hashing algorithm to use (default is "SHA-256").
     * @return The hexadecimal string representation of the checksum, or null if the file doesn't exist or an error occurs.
     */
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
