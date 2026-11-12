package ir.amirhesambandegan.easify_ui

import kotlin.test.Test
import kotlin.test.assertEquals

class EasifyUiHelperTest {
    @Test
    fun normalizeOtpInput_convertsPersianAndArabicDigits() {
        assertEquals("1234", normalizeOtpInput("۱۲۳۴", maxLength = 4, digitsOnly = true))
        assertEquals("5678", normalizeOtpInput("٥٦٧٨", maxLength = 4, digitsOnly = true))
        assertEquals("1234", normalizeOtpInput("12a3456", maxLength = 4, digitsOnly = true))
    }

    @Test
    fun normalizeOtpInput_allowsTextWhenDigitsOnlyDisabled() {
        assertEquals("ab12", normalizeOtpInput("ab۱۲!", maxLength = 4, digitsOnly = false))
    }
}
