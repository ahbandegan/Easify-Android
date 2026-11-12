package ir.amirhesambandegan.easify_auth

import ir.amirhesambandegan.easify_auth.sms.OtpExtractor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OtpExtractorTest {

    @Test
    fun testExtractStandardOtp() {
        val message = "Your verification code is 482910. Do not share this code with anyone."
        val otp = OtpExtractor.extractOtp(message, 6..6)
        assertEquals("482910", otp)
    }

    @Test
    fun testExtractPersianSmsWithEnglishDigits() {
        val message = "کد ورود شما به برنامه 7391 است. اعتبار: ۲ دقیقه"
        val otp = OtpExtractor.extractOtp(message, 4..4)
        assertEquals("7391", otp)
    }

    @Test
    fun testExtractPersianSmsWithPersianDigits() {
        val message = "کد فعالسازی شما: ۶۵۴۳۲۱\nEasify"
        val otp = OtpExtractor.extractOtp(message, 6..6)
        assertEquals("654321", otp)
    }

    @Test
    fun testPhoneNumberDoesNotMatchShortOtp() {
        // A phone number with 11 digits should not be extracted when range is 4..8
        val message = "لطفا با شماره 09123456789 تماس نگیرید. کد شما 8421 است."
        val otp = OtpExtractor.extractOtp(message, 4..6)
        assertEquals("8421", otp)
    }

    @Test
    fun testNoOtpInMessage() {
        val message = "Welcome to our application! No code here."
        val otp = OtpExtractor.extractOtp(message, 4..6)
        assertNull(otp)
    }

    @Test
    fun testDigitNormalization() {
        val persian = "۰۱۲۳۴۵۶۷۸۹"
        val normalized = OtpExtractor.normalizeDigits(persian)
        assertEquals("0123456789", normalized)
    }
}
