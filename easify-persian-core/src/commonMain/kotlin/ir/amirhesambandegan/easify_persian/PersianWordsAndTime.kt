package ir.amirhesambandegan.easify_persian

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private val units = arrayOf("", "یک", "دو", "سه", "چهار", "پنج", "شش", "هفت", "هشت", "نه")
private val teens = arrayOf("ده", "یازده", "دوازده", "سیزده", "چهارده", "پانزده", "شانزده", "هفده", "هجده", "نوزده")
private val tens = arrayOf("", "", "بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود")
private val hundreds = arrayOf("", "یکصد", "دویست", "سیصد", "چهارصد", "پانصد", "ششصد", "هفتصد", "هشتصد", "نهصد")
private val levels = arrayOf("", "هزار", "میلیون", "میلیارد", "تریلیارد", "کوآدریلیون", "کوینتیلیون")

/**
 * Converts any [Long] number (including full 64-bit magnitudes and [Long.MIN_VALUE])
 * into its Persian word representation.
 */
fun Long.toPersianWords(): String {
    if (this == 0L) return "صفر"
    if (this == Long.MIN_VALUE) {
        return "منفی نه کوینتیلیون و دویست و بیست و سه کوآدریلیون و سیصد و هفتاد و دو تریلیارد و سی و شش میلیارد و هشتصد و پنجاه و چهار میلیون و هفتصد و هفتاد و پنج هزار و هشتصد و هشت"
    }
    if (this < 0L) return "منفی " + (-this).toPersianWords()

    val groups = mutableListOf<String>()
    var num = this
    var level = 0

    while (num > 0L) {
        val threeDigits = (num % 1000L).toInt()
        if (threeDigits != 0) {
            val groupText = convertThreeDigits(threeDigits)
            val levelName = levels.getOrElse(level) { "" }
            val levelText = if (levelName.isEmpty()) groupText else "$groupText $levelName"
            groups.add(0, levelText)
        }
        num /= 1000L
        level++
    }

    return groups.joinToString(" و ")
}

private fun convertThreeDigits(n: Int): String {
    val res = mutableListOf<String>()
    val h = n / 100
    val t = (n % 100) / 10
    val u = n % 10

    if (h > 0) res.add(hundreds[h])

    if (t == 1) {
        res.add(teens[u])
    } else {
        if (t > 1) res.add(tens[t])
        if (u > 0) res.add(units[u])
    }

    return res.joinToString(" و ")
}

/**
 * Converts a [Number] into a Persian currency text representation (e.g. `15000.toPersianCurrencyText("تومان")`).
 */
fun Number.toPersianCurrencyText(unit: String = "تومان"): String {
    val words = toLong().toPersianWords()
    return if (unit.isBlank()) words else "$words $unit"
}

/**
 * Converts a Unix timestamp (in milliseconds) to a human-readable Persian "time ago" string.
 * Pure Kotlin Multiplatform implementation.
 */
@OptIn(ExperimentalTime::class)
fun Long.toPersianTimeAgo(
    nowMillis: Long = Clock.System.now().toEpochMilliseconds(),
    persianDigits: Boolean = true
): String {
    if (this <= 0L || this > nowMillis) return "لحظاتی پیش"
    val diff = nowMillis - this

    val text = when {
        diff < 60_000L -> "لحظاتی پیش"
        diff < 120_000L -> "یک دقیقه پیش"
        diff < 3_600_000L -> "${diff / 60_000L} دقیقه پیش"
        diff < 7_200_000L -> "یک ساعت پیش"
        diff < 86_400_000L -> "${diff / 3_600_000L} ساعت پیش"
        diff < 172_800_000L -> "دیروز"
        diff < 2_592_000_000L -> "${diff / 86_400_000L} روز پیش"
        diff < 5_184_000_000L -> "یک ماه پیش"
        diff < 31_536_000_000L -> "${diff / 2_592_000_000L} ماه پیش"
        diff < 63_072_000_000L -> "یک سال پیش"
        else -> "${diff / 31_536_000_000L} سال پیش"
    }
    return if (persianDigits) text.toPersianDigits() else text
}
