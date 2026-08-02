package ir.amirhesambandegan.easify_persian

/**
 * Converts a time in milliseconds to a human-readable Persian "time ago" string.
 * Example: "۵ دقیقه پیش", "دیروز".
 */
fun Long.toPersianTimeAgo(): String {
    val now = System.currentTimeMillis()
    val diff = now - this

    return when {
        diff < 60_000 -> "لحظاتی پیش"
        diff < 3600_000 -> "${(diff / 60_000)} دقیقه پیش"
        diff < 86400_000 -> "${(diff / 3600_000)} ساعت پیش"
        diff < 172800_000 -> "دیروز"
        diff < 2592000_000L -> "${(diff / 86400_000)} روز پیش"
        diff < 31536000_000L -> "${(diff / 2592000_000L)} ماه پیش"
        else -> "${(diff / 31536000_000L)} سال پیش"
    }.toPersianDigits()
}
