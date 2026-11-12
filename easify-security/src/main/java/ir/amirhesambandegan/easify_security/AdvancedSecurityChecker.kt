package ir.amirhesambandegan.easify_security

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.io.File
import java.security.MessageDigest

/**
 * Provides runtime integrity and environment checks including emulator detection,
 * hook detection (Frida / Xposed / Substrate), and cryptographic APK signing-certificate verification.
 */
object AdvancedSecurityChecker {

    /**
     * Checks whether the current device is likely an emulator by inspecting system build properties.
     */
    fun isEmulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.lowercase().contains("vbox") ||
            Build.FINGERPRINT.lowercase().contains("test-keys") ||
            Build.MODEL.contains("google_sdk") ||
            Build.MODEL.contains("Emulator") ||
            Build.MODEL.contains("Android SDK built for x86") ||
            Build.MANUFACTURER.contains("Genymotion") ||
            (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
            "google_sdk" == Build.PRODUCT ||
            Build.HARDWARE.contains("goldfish") ||
            Build.HARDWARE.contains("ranchu")
    }

    /**
     * Detects suspicious instrumentation/hooking frameworks (Xposed, Frida, Substrate)
     * in call stacks and loaded memory maps (`/proc/self/maps`).
     */
    fun hasDangerousHooks(): Boolean {
        val stackDetected = runCatching {
            throw Exception("Checking hooks")
        }.exceptionOrNull()?.stackTrace?.let { stack ->
            var hits = 0
            for (element in stack) {
                val cls = element.className.lowercase()
                if (cls.contains("xposedbridge") || cls.contains("frida") || cls.contains("substrate")) {
                    hits++
                }
            }
            hits > 0
        } ?: false

        if (stackDetected) return true

        val mapsDetected = runCatching {
            val mapsFile = File("/proc/self/maps")
            if (!mapsFile.canRead()) return@runCatching false
            mapsFile.useLines { lines ->
                lines.any { line ->
                    val lower = line.lowercase()
                    lower.contains("frida") || lower.contains("xposed") || lower.contains("libsubstrate")
                }
            }
        }.getOrDefault(false)

        return mapsDetected
    }

    /**
     * Returns all SHA-256 hex fingerprints of the APK signing certificates for the host package.
     */
    @Suppress("DEPRECATION")
    fun getAppSignaturesSha256(context: Context): List<String> {
        val pm = context.packageManager
        val pkgName = context.packageName
        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val info = pm.getPackageInfo(pkgName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signingInfo = info.signingInfo ?: return emptyList()
            if (signingInfo.hasMultipleSigners()) {
                signingInfo.apkContentsSigners.orEmpty()
            } else {
                signingInfo.signingCertificateHistory.orEmpty()
            }
        } else {
            pm.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES).signatures.orEmpty()
        }

        val digest = MessageDigest.getInstance("SHA-256")
        return signatures.map { sig ->
            digest.digest(sig.toByteArray()).joinToString("") { "%02x".format(it) }
        }
    }

    /**
     * Verifies the application's APK signing certificate against [expectedSignatureSha256]
     * (with or without colons, case-insensitive) using constant-time digest comparison.
     */
    fun verifyAppSignature(context: Context, expectedSignatureSha256: String): Boolean {
        val normalizedExpected = expectedSignatureSha256
            .replace(":", "")
            .replace(" ", "")
            .trim()
            .lowercase()

        if (normalizedExpected.length != 64 || !normalizedExpected.all { it in '0'..'9' || it in 'a'..'f' }) {
            return false
        }

        val expectedBytes = normalizedExpected.toByteArray(Charsets.US_ASCII)
        return runCatching {
            getAppSignaturesSha256(context).any { actualHex ->
                MessageDigest.isEqual(actualHex.toByteArray(Charsets.US_ASCII), expectedBytes)
            }
        }.getOrDefault(false)
    }
}
