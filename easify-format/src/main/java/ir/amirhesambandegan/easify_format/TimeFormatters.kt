package ir.amirhesambandegan.easify_format

import java.util.Locale

/**
 * Converts a timestamp (in milliseconds) into a human-readable "time ago" format in Persian.
 * E.g., "لحظاتی پیش" (just now), "دیروز" (yesterday), "۲ روز پیش" (2 days ago).
 *
 * @return A formatted string representing the time elapsed since the timestamp.
 */
fun Long.toTimeAgo(): String {
    val now = System.currentTimeMillis()
    if (this > now || this <= 0) return "لحظاتی پیش"
    val diff = now - this
    
    val minute = 60 * 1000L
    val hour = 60 * minute
    val day = 24 * hour
    val week = 7 * day
    val month = 30 * day
    val year = 365 * day

    return when {
        diff < minute -> "لحظاتی پیش"
        diff < 2 * minute -> "یک دقیقه پیش"
        diff < hour -> "${diff / minute} دقیقه پیش"
        diff < 2 * hour -> "یک ساعت پیش"
        diff < day -> "${diff / hour} ساعت پیش"
        diff < 2 * day -> "دیروز"
        diff < week -> "${diff / day} روز پیش"
        diff < month -> "${diff / week} هفته پیش"
        diff < 2 * month -> "یک ماه پیش"
        diff < year -> "${diff / month} ماه پیش"
        diff < 2 * year -> "یک سال پیش"
        else -> "${diff / year} سال پیش"
    }
}

/**
 * Converts a duration in seconds to a standard timer format string (HH:MM:SS or MM:SS).
 *
 * @return A formatted time string. If the duration is an hour or more, it uses "HH:MM:SS",
 * otherwise it uses "MM:SS".
 */
fun Long.toTimerFormat(): String {
    val totalSeconds = this
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }
}
