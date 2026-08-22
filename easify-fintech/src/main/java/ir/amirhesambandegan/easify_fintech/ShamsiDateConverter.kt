package ir.amirhesambandegan.easify_fintech

import java.util.Calendar

/**
 * Lightweight Shamsi (Jalali) Date Converter.
 */
class ShamsiDate(var year: Int, var month: Int, var day: Int) {
    override fun toString(): String {
        val m = if (month < 10) "0$month" else month.toString()
        val d = if (day < 10) "0$day" else day.toString()
        return "$year/$m/$d"
    }
}

fun Long.toShamsiDate(): ShamsiDate {
    val cal = Calendar.getInstance()
    cal.timeInMillis = this
    
    val gy = cal.get(Calendar.YEAR)
    val gm = cal.get(Calendar.MONTH) + 1
    val gd = cal.get(Calendar.DAY_OF_MONTH)
    
    val g_d_m = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
    var jy: Int
    val jm: Int
    val jd: Int
    
    val gy2 = if (gm > 2) gy + 1 else gy
    var days = 355666 + (365 * gy) + ((gy2 + 3) / 4) - ((gy2 + 99) / 100) + ((gy2 + 399) / 400) + gd + g_d_m[gm - 1]
    
    jy = -1595 + (33 * (days / 12053))
    days %= 12053
    jy += 4 * (days / 1461)
    days %= 1461
    if (days > 365) {
        jy += (days - 1) / 365
        days = (days - 1) % 365
    }
    if (days < 186) {
        jm = 1 + (days / 31)
        jd = 1 + (days % 31)
    } else {
        jm = 7 + ((days - 186) / 30)
        jd = 1 + ((days - 186) % 30)
    }
    
    return ShamsiDate(jy, jm, jd)
}
