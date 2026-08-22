package ir.amirhesambandegan.easify_persian

/**
 * Returns the Persian name of the month for this [JalaliDate].
 *
 * @return The Persian name of the month (e.g., "فروردین", "اردیبهشت"), or "نامشخص" if invalid.
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
 * Checks if the year is a Jalali leap year (سال کبیسه).
 *
 * @return `true` if the year is a leap year, `false` otherwise.
 */
fun JalaliDate.isLeapYear(): Boolean {
    val matches = intArrayOf(1, 5, 9, 13, 17, 22, 26, 30)
    val a = year % 33
    return matches.contains(a)
}
