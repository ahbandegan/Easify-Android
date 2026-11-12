package ir.amirhesambandegan.easify_persian

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PersianCoreTest {

    @Test
    fun jalaliRoundTripAndKnownDatesAreExact() {
        // Known exact date: 2026-11-12 Gregorian == 1405/08/21 Jalali (Thursday / پنج‌شنبه)
        val today = JalaliConverter.gregorianToJalali(2026, 11, 12)
        assertEquals(JalaliDate(1405, 8, 21), today)
        assertEquals("پنج‌شنبه", today.dayOfWeekName())

        val back = JalaliConverter.jalaliToGregorian(1405, 8, 21)
        assertEquals(JalaliConverter.GregorianDate(2026, 11, 12), back)

        // Nowruz 1405 == 2026-03-21
        assertEquals(JalaliDate(1405, 1, 1), JalaliConverter.gregorianToJalali(2026, 3, 21))
        assertEquals(JalaliConverter.GregorianDate(2026, 3, 21), JalaliConverter.jalaliToGregorian(1405, 1, 1))

        // Leap year check (1403 is leap in 33-year cycle: 1403 % 33 == 17 -> 30 days in Esfand)
        assertTrue(JalaliDate(1403, 12, 1).isLeapYear())
        assertEquals(30, JalaliDate(1403, 12, 1).daysInMonth())
        assertFalse(JalaliDate(1405, 12, 1).isLeapYear())
        assertEquals(29, JalaliDate(1405, 12, 1).daysInMonth())

        // Extensive round-trip sweep (1970..2050)
        for (year in 1350..1430 step 5) {
            for (month in 1..12) {
                val maxDay = JalaliDate(year, month, 1).daysInMonth()
                for (day in listOf(1, 15, maxDay)) {
                    val g = JalaliConverter.jalaliToGregorian(year, month, day)
                    val j = JalaliConverter.gregorianToJalali(g.year, g.month, g.day)
                    assertEquals(JalaliDate(year, month, day), j)
                }
            }
        }
    }

    @Test
    fun nationalCodeMobileAndDigitsWork() {
        assertTrue("0084575948".isValidNationalCode())
        assertTrue("۰۰۸۴۵۷۵۹۴۸".isValidNationalCode())
        assertFalse("0000000000".isValidNationalCode())
        assertFalse("1111111111".isValidNationalCode())

        assertTrue("۰۹۱۲۳۴۵۶۷۸۹".isValidPersianMobile())
        assertEquals("0912", "۰۹۱۲".toEnglishDigits())
        assertEquals("سلام ۱۴۰۰", "سلام 1400".toStandardPersian())
    }

    @Test
    fun persianWordsHandleLargeAndNegativeNumbersWithoutCrashing() {
        assertEquals("صفر", 0L.toPersianWords())
        assertEquals("منفی پانصد", (-500L).toPersianWords())
        assertTrue(1_000_000_000_000_000L.toPersianWords().contains("کوآدریلیون"))
        assertTrue(Long.MIN_VALUE.toPersianWords().startsWith("منفی"))
        assertEquals("پانزده هزار تومان", 15000.toPersianCurrencyText())
    }
}
