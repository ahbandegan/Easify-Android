package ir.amirhesambandegan.easify_file

import android.util.Base64
import java.io.File
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

private val FILE_UNITS = arrayOf("B", "KB", "MB", "GB", "TB", "PB", "EB")

fun Long.toReadableFileSize(): String {
    if (this <= 0L) return "0 B"
    val digitGroups = (log10(toDouble()) / log10(1024.0))
        .toInt()
        .coerceIn(0, FILE_UNITS.lastIndex)
    val symbols = DecimalFormatSymbols(Locale.US)
    return DecimalFormat("#,##0.##", symbols)
        .format(this / 1024.0.pow(digitGroups.toDouble())) + " " + FILE_UNITS[digitGroups]
}

fun File.toBase64String(): String {
    return try {
        Base64.encodeToString(readBytes(), Base64.NO_WRAP)
    } catch (_: Exception) {
        ""
    }
}

fun String.saveBase64ToFile(destination: File): Boolean {
    return try {
        destination.parentFile?.mkdirs()
        val decodedBytes = Base64.decode(this, Base64.NO_WRAP)
        destination.writeBytes(decodedBytes)
        true
    } catch (_: Exception) {
        false
    }
}

fun File.calculateMD5(): String = calculateHash("MD5")

fun File.calculateSHA256(): String = calculateHash("SHA-256")

private fun File.calculateHash(algorithm: String): String {
    return try {
        val digest = MessageDigest.getInstance(algorithm)
        inputStream().use { input ->
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }
        digest.digest().joinToString("") { "%02x".format(it) }
    } catch (_: Exception) {
        ""
    }
}
