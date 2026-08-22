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
 * Property extensions for quick access to Android System Services.
 * Eliminates the need for casting and getSystemService boilerplate.
 */

val Context.inputMethodManager: InputMethodManager?
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

val Context.connectivityManager: ConnectivityManager?
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

val Context.notificationManager: NotificationManager?
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

val Context.clipboardManager: ClipboardManager?
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

val Context.windowManager: WindowManager?
    get() = getSystemService(Context.WINDOW_SERVICE) as? WindowManager

val Context.alarmManager: AlarmManager?
    get() = getSystemService(Context.ALARM_SERVICE) as? AlarmManager

val Context.locationManager: LocationManager?
    get() = getSystemService(Context.LOCATION_SERVICE) as? LocationManager

val Context.powerManager: PowerManager?
    get() = getSystemService(Context.POWER_SERVICE) as? PowerManager

val Context.batteryManager: BatteryManager?
    get() = getSystemService(Context.BATTERY_SERVICE) as? BatteryManager

val Context.audioManager: AudioManager?
    get() = getSystemService(Context.AUDIO_SERVICE) as? AudioManager

val Context.sensorManager: SensorManager?
    get() = getSystemService(Context.SENSOR_SERVICE) as? SensorManager

val Context.telephonyManager: TelephonyManager?
    get() = getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

val Context.activityManager: ActivityManager?
    get() = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager

val Context.vibrator: Vibrator?
    get() = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
