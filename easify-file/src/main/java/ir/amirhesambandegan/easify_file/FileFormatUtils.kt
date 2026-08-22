package ir.amirhesambandegan.easify_file

import android.util.Base64
import java.io.File
import java.security.MessageDigest
import kotlin.math.log10
import kotlin.math.pow
import java.text.DecimalFormat

fun Long.toReadableFileSize(): String {
    if (this <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.##").format(this / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
}

fun File.toBase64String(): String {
    return try {
        val bytes = this.readBytes()
        Base64.encodeToString(bytes, Base64.NO_WRAP)
    } catch (e: Exception) {
        ""
    }
}

fun String.saveBase64ToFile(destination: File): Boolean {
    return try {
        val decodedBytes = Base64.decode(this, Base64.NO_WRAP)
        destination.writeBytes(decodedBytes)
        true
    } catch (e: Exception) {
        false
    }
}

fun File.calculateMD5(): String = calculateHash("MD5")

fun File.calculateSHA256(): String = calculateHash("SHA-256")

private fun File.calculateHash(algorithm: String): String {
    return try {
        val digest = MessageDigest.getInstance(algorithm)
        this.inputStream().use { input ->
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }
        digest.digest().joinToString("") { "%02x".format(it) }
    } catch (e: Exception) {
        ""
    }
}
