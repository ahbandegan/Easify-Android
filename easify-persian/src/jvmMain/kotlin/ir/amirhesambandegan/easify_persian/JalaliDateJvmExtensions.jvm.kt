package ir.amirhesambandegan.easify_persian

import java.util.Calendar
import java.util.Date

/**
 * Converts a Gregorian [Date] to a [JalaliDate].
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
 */
fun JalaliDate.toGregorian(): Date {
    val greg = JalaliConverter.jalaliToGregorian(year, month, day)
    val calendar = Calendar.getInstance()
    calendar.set(greg.year, greg.month - 1, greg.day, 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}
