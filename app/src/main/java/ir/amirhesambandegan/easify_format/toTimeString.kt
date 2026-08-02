package ir.amirhesambandegan.easify_format

import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Converts a duration in milliseconds to a formatted time string (HH:mm:ss or mm:ss).
 *
 * @return A formatted time string. If the duration is an hour or more, it uses "HH:mm:ss",
 * otherwise it uses "mm:ss".
 */
fun Long.toTimeString(): String {
    val hours = TimeUnit.MILLISECONDS.toHours(this)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60
    return if (hours > 0) {
        String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}