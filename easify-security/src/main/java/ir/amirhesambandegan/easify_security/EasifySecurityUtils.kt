package ir.amirhesambandegan.easify_security

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import java.io.File

/**
 * A utility object containing various security checks such as root detection,
 * VPN status, and developer options verification.
 */
object EasifySecurityUtils {

    /**
     * Checks whether the device is rooted by looking for common su binaries and paths.
     *
     * @return `true` if evidence of rooting is found, `false` otherwise.
     */
    fun isRooted(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )
        return paths.any { File(it).exists() } || checkSuCommand()
    }

    /**
     * Attempts to execute the 'su' command to verify root access dynamically.
     *
     * @return `true` if the 'su' command is available and executable, `false` otherwise.
     */
    private fun checkSuCommand(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val result = process.inputStream.bufferedReader().use { it.readText() }
            result.isNotEmpty()
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Checks if a VPN connection is currently active on the device.
     *
     * @param context The application context used to access connectivity services.
     * @return `true` if an active VPN transport is detected, `false` otherwise.
     */
    fun isVpnActive(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val caps = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }

    /**
     * Determines whether developer options are enabled on the device.
     *
     * @param context The application context used to query system settings.
     * @return `true` if developer options are enabled, `false` otherwise.
     */
    fun isDevOptionsEnabled(context: Context): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
            0
        ) != 0
    }
}