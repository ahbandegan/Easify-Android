package ir.amirhesambandegan.easify_biometric

import android.content.Context
import android.content.pm.PackageManager

enum class BiometricHardwareType {
    FACE, FINGERPRINT, IRIS, NONE
}

object HardwareTypeAnalyzer {
    fun getHardwareType(context: Context): BiometricHardwareType {
        val pm = context.packageManager
        return when {
            pm.hasSystemFeature(PackageManager.FEATURE_FACE) -> BiometricHardwareType.FACE
            pm.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT) -> BiometricHardwareType.FINGERPRINT
            pm.hasSystemFeature(PackageManager.FEATURE_IRIS) -> BiometricHardwareType.IRIS
            else -> BiometricHardwareType.NONE
        }
    }
}
