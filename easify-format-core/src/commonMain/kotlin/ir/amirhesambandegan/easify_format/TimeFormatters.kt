package ir.amirhesambandegan.easify_format

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private fun Long.padTwo(): String = toString().padStart(2, '0')

/**
 * Converts a Unix timestamp (in milliseconds) into a human-readable "time ago" format in Persian.
 * Pure Kotlin Multiplatform implementation.
 */
@OptIn(ExperimentalTime::class)
fun Long.toTimeAgo(nowMillis: Long = Clock.System.now().toEpochMilliseconds()): String {
    if (this > nowMillis || this <= 0L) return "لحظاتی پیش"
    val diff = nowMillis - this

    val minute = 60_000L
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
 * Converts a duration in seconds to a standard timer string (`HH:MM:SS` or `MM:SS`).
 */
fun Long.toTimerFormat(): String {
    val totalSeconds = coerceAtLeast(0L)
    val hours = totalSeconds / 3600L
    val minutes = (totalSeconds % 3600L) / 60L
    val seconds = totalSeconds % 60L
    return if (hours > 0L) {
        "${hours.padTwo()}:${minutes.padTwo()}:${seconds.padTwo()}"
    } else {
        "${minutes.padTwo()}:${seconds.padTwo()}"
    }
}

/**
 * Converts a duration in milliseconds to a formatted time string (`HH:mm:ss` or `mm:ss`).
 */
fun Long.toTimeString(): String {
    val safeMillis = coerceAtLeast(0L)
    return (safeMillis / 1000L).toTimerFormat()
}

/**
 * Formats a duration in seconds into a human-readable breakdown such as `"2d 3h 15m 4s"`.
 */
fun Long.toReadableDuration(includeSeconds: Boolean = true): String {
    val totalSeconds = coerceAtLeast(0L)
    if (totalSeconds == 0L) return if (includeSeconds) "0s" else "0m"

    val days = totalSeconds / 86_400L
    val hours = (totalSeconds % 86_400L) / 3_600L
    val minutes = (totalSeconds % 3_600L) / 60L
    val seconds = totalSeconds % 60L

    val parts = buildList {
        if (days > 0L) add("${days}d")
        if (hours > 0L) add("${hours}h")
        if (minutes > 0L) add("${minutes}m")
        if (includeSeconds && (seconds > 0L || isEmpty())) add("${seconds}s")
    }
    return if (parts.isEmpty()) "0m" else parts.joinToString(" ")
}
