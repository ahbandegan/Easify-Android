package ir.amirhesambandegan.easify_persian

import kotlin.test.Test
import kotlin.test.assertEquals

class PersianUiHelpersTest {

    @Test
    fun clampJalaliDayRespectsLeapAndNonLeapEsfand() {
        // 1403 is leap in 33-year cycle -> Esfand has 30 days
        assertEquals(30, clampJalaliDay(1403, 12, 31))
        // 1404 is non-leap -> Esfand has 29 days
        assertEquals(29, clampJalaliDay(1404, 12, 30))
        // Mehr (month 7) has 30 days
        assertEquals(30, clampJalaliDay(1404, 7, 31))
        // Farvardin (month 1) has 31 days
        assertEquals(31, clampJalaliDay(1404, 1, 31))
    }
}
