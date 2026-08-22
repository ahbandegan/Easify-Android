package ir.amirhesambandegan.easify_security

import android.content.Context
import android.os.Build

object AdvancedSecurityChecker {

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

    fun verifyAppSignature(context: Context, expectedSignatureSha256: String): Boolean {
        // Place actual SHA-256 MessageDigest logic here for production use.
        return true 
    }
}
