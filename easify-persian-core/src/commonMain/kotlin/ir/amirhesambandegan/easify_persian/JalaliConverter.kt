package ir.amirhesambandegan.easify_persian

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Represents a Jalali (Solar Hijri / Persian) calendar date.
 *
 * @property year The Jalali year (e.g. `1405`).
 * @property month The Jalali month (`1` = Farvardin .. `12` = Esfand).
 * @property day The day of the Jalali month (`1`..`31`).
 */
data class JalaliDate(val year: Int, val month: Int, val day: Int) {

    /**
     * Returns the date formatted as `"YYYY/MM/DD"`.
     */
    override fun toString(): String {
        val y = year.toString().padStart(4, '0')
        val m = month.toString().padStart(2, '0')
        val d = day.toString().padStart(2, '0')
        return "$y/$m/$d"
    }

    /**
     * Returns the date formatted with customizable separators or Persian digits.
     */
    fun format(separator: String = "/", persianDigits: Boolean = false): String {
        val y = year.toString().padStart(4, '0')
        val m = month.toString().padStart(2, '0')
        val d = day.toString().padStart(2, '0')
        val raw = "$y$separator$m$separator$d"
        return if (persianDigits) raw.toPersianDigits() else raw
    }

    /**
     * Returns a full readable Persian date string such as `"پنج‌شنبه ۲۱ آبان ۱۴۰۵"`.
     */
    fun toReadablePersianDate(includeWeekday: Boolean = true, persianDigits: Boolean = true): String {
        val base = "$day ${getMonthName()} $year"
        val full = if (includeWeekday) "${dayOfWeekName()} $base" else base
        return if (persianDigits) full.toPersianDigits() else full
    }

    companion object {
        /**
         * Returns the current [JalaliDate] for the given UTC offset (defaults to Iran Standard Time UTC+03:30).
         */
        @OptIn(ExperimentalTime::class)
        fun now(
            utcOffsetSeconds: Int = 3 * 3600 + 30 * 60,
            nowMillis: Long = Clock.System.now().toEpochMilliseconds()
        ): JalaliDate = epochMillisToJalali(nowMillis, utcOffsetSeconds)

        /**
         * Converts Unix epoch milliseconds to [JalaliDate] in pure Kotlin Multiplatform.
         */
        fun fromEpochMillis(
            epochMillis: Long,
            utcOffsetSeconds: Int = 0
        ): JalaliDate = epochMillisToJalali(epochMillis, utcOffsetSeconds)
    }
}

/**
 * Pure multiplatform Gregorian <-> Jalali converter using exact JDF arithmetic.
 */
object JalaliConverter {

    /**
     * A Gregorian calendar date tuple used by [JalaliConverter].
     */
    data class GregorianDate(val year: Int, val month: Int, val day: Int)

    private val GREGORIAN_ACCUMULATED_DAYS = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)

    /**
     * Converts a Gregorian date ([gy], [gm] `1..12`, [gd] `1..31`) to a [JalaliDate].
     */
    fun gregorianToJalali(gy: Int, gm: Int, gd: Int): JalaliDate {
        require(gm in 1..12) { "Gregorian month must be in 1..12, was $gm" }
        val gy2 = if (gm > 2) gy + 1 else gy
        var days = 355666 + (365 * gy) + ((gy2 + 3) / 4) - ((gy2 + 99) / 100) + ((gy2 + 399) / 400) +
            gd + GREGORIAN_ACCUMULATED_DAYS[gm - 1]
        var jy = -1595 + (33 * (days / 12053))
        days %= 12053
        jy += 4 * (days / 1461)
        days %= 1461
        if (days > 365) {
            jy += (days - 1) / 365
            days = (days - 1) % 365
        }
        val jm: Int
        val jd: Int
        if (days < 186) {
            jm = 1 + (days / 31)
            jd = 1 + (days % 31)
        } else {
            jm = 7 + ((days - 186) / 30)
            jd = 1 + ((days - 186) % 30)
        }
        return JalaliDate(jy, jm, jd)
    }

    /**
     * Converts a Jalali date ([jy], [jm] `1..12`, [jd] `1..31`) to a [GregorianDate].
     */
    fun jalaliToGregorian(jy: Int, jm: Int, jd: Int): GregorianDate {
        require(jm in 1..12) { "Jalali month must be in 1..12, was $jm" }
        val j = jy + 1595
        var days = -355668 + (365 * j) + ((j / 33) * 8) + (((j % 33) + 3) / 4) + jd +
            if (jm < 7) (jm - 1) * 31 else ((jm - 7) * 30) + 186

        var gy = 400 * (days / 146097)
        days %= 146097
        if (days > 36524) {
            gy += 100 * (--days / 36524)
            days %= 36524
            if (days >= 365) days++
        }
        gy += 4 * (days / 1461)
        days %= 1461
        if (days > 365) {
            gy += (days - 1) / 365
            days = (days - 1) % 365
        }
        var gd = days + 1
        val isLeap = (gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)
        val monthDays = intArrayOf(0, 31, if (isLeap) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var gm = 0
        while (gm < 12 && gd > monthDays[gm + 1]) {
            gd -= monthDays[gm + 1]
            gm++
        }
        return GregorianDate(gy, gm + 1, gd)
    }
}

/**
 * Howard Hinnant civil-from-days algorithm for pure multiplatform epoch conversion without JVM/Android APIs.
 */
internal fun epochMillisToJalali(epochMillis: Long, utcOffsetSeconds: Int = 0): JalaliDate {
    val adjustedMillis = epochMillis + utcOffsetSeconds * 1000L
    val daysSinceEpoch = adjustedMillis.floorDiv(86_400_000L)
    val z = daysSinceEpoch + 719468L
    val era = (if (z >= 0) z else z - 146096L) / 146097L
    val doe = (z - era * 146097L).toInt()
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
    val y = yoe + era.toInt() * 400
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp = (5 * doy + 2) / 153
    val d = doy - (153 * mp + 2) / 5 + 1
    val m = mp + (if (mp < 10) 3 else -9)
    val gy = y + (if (m <= 2) 1 else 0)
    return JalaliConverter.gregorianToJalali(gy, m, d)
}

/**
 * Returns the Persian name of the month (`فروردین` .. `اسفند`), or `"نامشخص"` if out of range.
 */
fun JalaliDate.getMonthName(): String {
    val months = arrayOf(
        "فروردین", "اردیبهشت", "خرداد",
        "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر",
        "دی", "بهمن", "اسفند"
    )
    return if (month in 1..12) months[month - 1] else "نامشخص"
}

/**
 * Checks whether the Jalali [year] is a leap year (سال کبیسه) using the 33-year cycle.
 */
fun isJalaliLeapYear(year: Int): Boolean {
    val mod = ((year % 33) + 33) % 33
    return mod == 1 || mod == 5 || mod == 9 || mod == 13 || mod == 17 || mod == 22 || mod == 26 || mod == 30
}

/**
 * Checks if the year of this [JalaliDate] is a Jalali leap year (سال کبیسه).
 */
fun JalaliDate.isLeapYear(): Boolean = isJalaliLeapYear(year)

/**
 * Returns the exact number of days in the month of this [JalaliDate] (`29`, `30`, or `31`).
 */
fun JalaliDate.daysInMonth(): Int = when {
    month in 1..6 -> 31
    month in 7..11 -> 30
    month == 12 -> if (isLeapYear()) 30 else 29
    else -> 0
}

/**
 * Returns the Iranian week day index (`0 = شنبه` .. `6 = جمعه`) using Sakamoto/Gregorian weekday mapping.
 */
fun JalaliDate.dayOfWeek(): Int {
    val g = JalaliConverter.jalaliToGregorian(year, month, day)
    val t = intArrayOf(0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4)
    val y = if (g.month < 3) g.year - 1 else g.year
    // 0 = Sunday, 1 = Monday, ..., 6 = Saturday in Sakamoto formula
    val sundayBased = ((y + y / 4 - y / 100 + y / 400 + t[g.month - 1] + g.day) % 7 + 7) % 7
    // Convert so 0 = Saturday (شنبه), 1 = Sunday, ..., 6 = Friday (جمعه)
    return (sundayBased + 1) % 7
}

/**
 * Returns the Persian weekday name for this [JalaliDate] (`شنبه` .. `جمعه`).
 */
fun JalaliDate.dayOfWeekName(): String {
    val names = arrayOf("شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه")
    return names[dayOfWeek()]
}
