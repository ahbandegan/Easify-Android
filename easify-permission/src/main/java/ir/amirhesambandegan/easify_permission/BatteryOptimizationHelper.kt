package ir.amirhesambandegan.easify_permission

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings

/**
 * Helper utility object to check and request exemptions from Android battery optimizations (Doze mode).
 */
object BatteryOptimizationHelper {

    /**
     * Checks whether the application is currently ignoring battery optimizations (whitelisted from Doze mode).
     *
     * @param context The application or activity [Context].
     * @return `true` if the app is exempt from battery optimizations or if running on API level < 23 (where Doze mode does not exist), `false` otherwise.
     */
    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            powerManager.isIgnoringBatteryOptimizations(context.packageName)
        } else {
            true // Not applicable before API 23
        }
    }

    /**
     * Requests the user to whitelist the application to ignore battery optimizations.
     *
     * Launches the system dialog using [Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS]
     * if the application is not already ignoring battery optimizations on Android M (API 23) and above.
     *
     * @param context The application or activity [Context] used to start the intent.
     */
    @SuppressLint("BatteryLife")
    fun requestIgnoreBatteryOptimizations(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isIgnoringBatteryOptimizations(context)) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:${context.packageName}")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            }
        }
    }
}
