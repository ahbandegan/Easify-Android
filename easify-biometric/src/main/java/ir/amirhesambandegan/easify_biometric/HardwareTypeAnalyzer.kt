package ir.amirhesambandegan.easify_biometric

import android.content.Context
import android.content.pm.PackageManager

/**
 * Represents the type of biometric hardware available on the device.
 */
enum class BiometricHardwareType {
    /** Face recognition hardware. */
    FACE, 
    /** Fingerprint recognition hardware. */
    FINGERPRINT, 
    /** Iris recognition hardware. */
    IRIS, 
    /** No biometric hardware detected. */
    NONE
}

/**
 * Utility object for analyzing the type of biometric hardware present on the device.
 */
object HardwareTypeAnalyzer {
    
    /**
     * Determines the available biometric hardware type based on device system features.
     * 
     * @param context The context used to access the package manager.
     * @return The detected [BiometricHardwareType]. Returns [BiometricHardwareType.NONE] if no hardware is found.
     */
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
