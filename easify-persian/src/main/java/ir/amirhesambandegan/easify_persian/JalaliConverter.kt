package ir.amirhesambandegan.easify_persian

import java.util.Calendar
import java.util.Date

/**
 * A data class representing a Jalali (Persian) date.
 *
 * @property year The Jalali year (e.g., 1403).
 * @property month The Jalali month (1 to 12).
 * @property day The day of the Jalali month (1 to 31).
 */
data class JalaliDate(val year: Int, val month: Int, val day: Int) {
    /**
     * Returns a string representation of the Jalali date in the format "YYYY/MM/DD".
     *
     * @return Formatted date string.
     */
    override fun toString(): String = String.format("%04d/%02d/%02d", year, month, day)
}

/**
 * Converts a Gregorian [Date] to a [JalaliDate].
 *
 * @return A [JalaliDate] instance corresponding to this Gregorian date.
 */
fun Date.toJalali(): JalaliDate {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return JalaliConverter.gregorianToJalali(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}

/**
 * Converts a [JalaliDate] to a Gregorian [Date].
 *
 * @return A [Date] instance corresponding to this Jalali date.
 */
fun JalaliDate.toGregorian(): Date {
    val greg = JalaliConverter.jalaliToGregorian(year, month, day)
    val calendar = Calendar.getInstance()
    calendar.set(greg.year, greg.month - 1, greg.day)
    return calendar.time
}

/**
 * Internal utility object containing logic for converting between Gregorian and Jalali dates.
 */
private object JalaliConverter {
    /**
     * A data class representing a Gregorian date internally used by the converter.
     *
     * @property year The Gregorian year.
     * @property month The Gregorian month (1 to 12).
     * @property day The day of the Gregorian month (1 to 31).
     */
    class GregorianDate(val year: Int, val month: Int, val day: Int)

    /**
     * Converts a specific Gregorian date to a Jalali date.
     *
     * @param gy The Gregorian year.
     * @param gm The Gregorian month.
     * @param gd The day of the Gregorian month.
     * @return A [JalaliDate] representing the equivalent Jalali date.
     */
    fun gregorianToJalali(gy: Int, gm: Int, gd: Int): JalaliDate {
        val gDaysInMonth = intArrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var gDays = 365 * (gy - 1) + (gy - 1) / 4 - (gy - 1) / 100 + (gy - 1) / 400 + gd
        for (i in 1 until gm) gDays += gDaysInMonth[i]
        if (gm > 2 && ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0))) gDays++

        val daysSinceJalaliEpoch = gDays - 226894
        var jy = 979 + 33 * (daysSinceJalaliEpoch / 12053)
        var daysLeft = daysSinceJalaliEpoch % 12053
        
        jy += 4 * (daysLeft / 1461)
        daysLeft %= 1461
        if (daysLeft >= 366) {
            jy += (daysLeft - 1) / 365
            daysLeft = (daysLeft - 1) % 365
        }

        val jm: Int
        val jd: Int
        if (daysLeft < 186) {
            jm = 1 + (daysLeft / 31)
            jd = 1 + (daysLeft % 31)
        } else {
            jm = 7 + ((daysLeft - 186) / 30)
            jd = 1 + ((daysLeft - 186) % 30)
        }
        return JalaliDate(jy, jm, jd)
    }

    /**
     * Converts a specific Jalali date to a Gregorian date.
     *
     * @param jy The Jalali year.
     * @param jm The Jalali month.
     * @param jd The day of the Jalali month.
     * @return A [GregorianDate] representing the equivalent Gregorian date.
     */
    fun jalaliToGregorian(jy: Int, jm: Int, jd: Int): GregorianDate {
        val jy2 = jy - 979
        var jDays = 365 * jy2 + (jy2 / 33) * 8 + (jy2 % 33 + 3) / 4
        for (i in 0 until jm - 1) jDays += if (i < 6) 31 else 30
        jDays += jd + 79
        
        var gy = 1600 + 400 * (jDays / 146097)
        var daysLeft = jDays % 146097
        var leap = true
        if (daysLeft >= 36525) {
            daysLeft--
            gy += 100 * (daysLeft / 36524)
            daysLeft %= 36524
            if (daysLeft >= 365) daysLeft++ else leap = false
        }
        gy += 4 * (daysLeft / 1461)
        daysLeft %= 1461
        if (daysLeft >= 366) {
            leap = false
            daysLeft--
            gy += daysLeft / 365
            daysLeft %= 365
        }
        val gDaysInMonth = intArrayOf(31, if (leap) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var gm = 0
        while (gm < 12 && daysLeft >= gDaysInMonth[gm]) {
            daysLeft -= gDaysInMonth[gm]
            gm++
        }
        return GregorianDate(gy, gm + 1, daysLeft + 1)
    }
}
