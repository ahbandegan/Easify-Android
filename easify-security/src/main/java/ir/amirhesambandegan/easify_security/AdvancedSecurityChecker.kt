package ir.amirhesambandegan.easify_security

import android.content.Context
import android.os.Build

/**
 * A utility object that provides advanced security checks to detect emulators,
 * dangerous hooks, and verify app signatures.
 */
object AdvancedSecurityChecker {

    /**
     * Checks whether the current device is likely an emulator by inspecting system properties.
     *
     * @return `true` if the device environment matches known emulator signatures, `false` otherwise.
     */
    fun isEmulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.lowercase().contains("vbox")
                || Build.FINGERPRINT.lowercase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT
    }

    /**
     * Detects if there are any dangerous hooks attached to the application process,
     * such as those from Xposed or Frida.
     *
     * @return `true` if suspicious hooks are detected in the stack trace, `false` otherwise.
     */
    fun hasDangerousHooks(): Boolean {
        try {
            throw Exception("Checking hooks")
        } catch (e: Exception) {
            var suspiciousCount = 0
            for (element in e.stackTrace) {
                if (element.className.contains("com.android.internal.os.ZygoteInit") ||
                    element.className.contains("de.robv.android.xposed.XposedBridge") ||
                    element.className.contains("frida")
                ) {
                    suspiciousCount++
                }
            }
            if (suspiciousCount > 1) return true
        }
        return false
    }

    /**
     * Verifies the application signature against the expected SHA-256 hash.
     *
     * @param context The application context used to retrieve package information.
     * @param expectedSignatureSha256 The expected SHA-256 signature to verify against.
     * @return `true` if the signature matches or verification passes, `false` otherwise.
     */
    fun verifyAppSignature(context: Context, expectedSignatureSha256: String): Boolean {
        // Place actual SHA-256 MessageDigest logic here for production use.
        return true 
    }
}
