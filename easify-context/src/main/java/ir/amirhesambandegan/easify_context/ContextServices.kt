package ir.amirhesambandegan.easify_context

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.ClipboardManager
import android.content.Context
import android.hardware.SensorManager
import android.location.LocationManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.PowerManager
import android.os.Vibrator
import android.telephony.TelephonyManager
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

/**
 * Property extensions for quick, type-safe access to common Android System Services.
 * 
 * These extensions eliminate the need for repetitive `getSystemService()` calls and 
 * unsafe type casting throughout your application codebase. They seamlessly handle 
 * potential nullability since system services can occasionally be unavailable 
 * depending on the API level or device state.
 */

/**
 * Convenience property to access the [InputMethodManager].
 * Used for managing the software keyboard.
 */
val Context.inputMethodManager: InputMethodManager?
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

/**
 * Convenience property to access the [ConnectivityManager].
 * Used for querying network state and connectivity capabilities.
 */
val Context.connectivityManager: ConnectivityManager?
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

/**
 * Convenience property to access the [NotificationManager].
 * Used for displaying and managing system notifications.
 */
val Context.notificationManager: NotificationManager?
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

/**
 * Convenience property to access the [ClipboardManager].
 * Used for copying and pasting text or data to/from the system clipboard.
 */
val Context.clipboardManager: ClipboardManager?
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

/**
 * Convenience property to access the [WindowManager].
 * Used for retrieving display metrics or adding custom views directly to the window.
 */
val Context.windowManager: WindowManager?
    get() = getSystemService(Context.WINDOW_SERVICE) as? WindowManager

/**
 * Convenience property to access the [AlarmManager].
 * Used for scheduling future tasks, broadcast intents, or precise timing operations.
 */
val Context.alarmManager: AlarmManager?
    get() = getSystemService(Context.ALARM_SERVICE) as? AlarmManager

/**
 * Convenience property to access the [LocationManager].
 * Used for querying GPS state and requesting location updates.
 */
val Context.locationManager: LocationManager?
    get() = getSystemService(Context.LOCATION_SERVICE) as? LocationManager

/**
 * Convenience property to access the [PowerManager].
 * Used for managing device wake locks and checking battery optimization status.
 */
val Context.powerManager: PowerManager?
    get() = getSystemService(Context.POWER_SERVICE) as? PowerManager

/**
 * Convenience property to access the [BatteryManager].
 * Used for querying current battery level, charging status, and health.
 */
val Context.batteryManager: BatteryManager?
    get() = getSystemService(Context.BATTERY_SERVICE) as? BatteryManager

/**
 * Convenience property to access the [AudioManager].
 * Used for controlling volume, ringer modes, and audio routing.
 */
val Context.audioManager: AudioManager?
    get() = getSystemService(Context.AUDIO_SERVICE) as? AudioManager

/**
 * Convenience property to access the [SensorManager].
 * Used for accessing device hardware sensors like accelerometers and gyroscopes.
 */
val Context.sensorManager: SensorManager?
    get() = getSystemService(Context.SENSOR_SERVICE) as? SensorManager

/**
 * Convenience property to access the [TelephonyManager].
 * Used for querying cellular network information, SIM state, and device identifiers.
 */
val Context.telephonyManager: TelephonyManager?
    get() = getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

/**
 * Convenience property to access the [ActivityManager].
 * Used for querying running processes, memory info, and task management.
 */
val Context.activityManager: ActivityManager?
    get() = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager

/**
 * Convenience property to access the [Vibrator] service.
 * Used for triggering haptic feedback and device vibrations.
 */
val Context.vibrator: Vibrator?
    get() = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
