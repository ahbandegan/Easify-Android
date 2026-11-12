package ir.amirhesambandegan.easify_validation

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidationRegexesTest {

    @Test
    fun emailValidationWorks() {
        assertTrue("user.name+tag@sub.example.com".isValidEmail())
        assertFalse("invalid-email@".isValidEmail())
        assertFalse("@example.com".isValidEmail())
    }

    @Test
    fun mobileValidationSupportsPersianDigitsAndOptionalInternational() {
        assertTrue("09123456789".isValidMobile())
        assertTrue("۰۹۱۲۳۴۵۶۷۸۹".isValidMobile())
        assertFalse("+989123456789".isValidMobile(allowInternational = false))
        assertTrue("+989123456789".isValidMobile(allowInternational = true))
        assertTrue("00989123456789".isValidMobile(allowInternational = true))
        assertFalse("08123456789".isValidMobile())
    }

    @Test
    fun nationalIdValidatesChecksumAndRejectsRepeatingDigits() {
        assertTrue("0084575948".isValidNationalId())
        assertFalse("1111111111".isValidNationalId())
        assertFalse("0000000000".isValidNationalId())
        assertFalse("1234567890".isValidNationalId())
        assertFalse("0084575949".isValidNationalId())
    }

    @Test
    fun urlValidationSupportsSubdomainsPortsQueriesAndFragments() {
        assertTrue("https://api.v2.easify.ir:8443/docs/v3?lang=fa#install".isValidUrl())
        assertTrue("example.co.uk/path-with_chars".isValidUrl())
        assertFalse("not a url".isValidUrl())
    }

    @Test
    fun strongPasswordAndCustomPolicyWork() {
        assertTrue("Strong#Pass1".isValidStrongPassword())
        assertTrue("Pass_Word123!".isValidStrongPassword())
        assertFalse("weakpass1".isValidStrongPassword())
        assertTrue("Simple12".isValidPassword(PasswordPolicy(minLength = 6, requireSpecialChar = false)))
        assertFalse("Simple 12!".isValidPassword(PasswordPolicy(allowWhitespace = false)))
    }

    @Test
    fun persianTextSupportsHalfSpaceZwnj() {
        assertTrue("می\u200Cخواهم تست کنم".isPersianText())
        assertFalse("Hello سلام".isPersianText())
    }

    @Test
    fun technicalAndNetworkValidatorsWork() {
        assertTrue("#FF6600".isValidHexColor())
        assertTrue("#80FFFFFF".isValidHexColor())
        assertFalse("#12345".isValidHexColor())

        assertTrue("192.168.1.1".isValidIpv4())
        assertFalse("256.1.1.1".isValidIpv4())

        assertTrue("2001:0db8:85a3:0000:0000:8a2e:0370:7334".isValidIpv6())
        assertTrue("2001:db8::1".isValidIpv6())
        assertTrue("::1".isValidIpv6())
        assertFalse("2001::85a3::1".isValidIpv6())

        assertTrue("550e8400-e29b-41d4-a716-446655440000".isValidUuid())
        assertFalse("550e8400-e29b-41d4-a716-44665544000z".isValidUuid())

        assertTrue("easify-kmp-core-3".isValidSlug())
        assertFalse("Easify_Bad".isValidSlug())

        assertTrue("443".isValidPort())
        assertTrue("۴۴۳".isValidPort())
        assertFalse("0".isValidPort(allowZero = false))
        assertTrue("0".isValidPort(allowZero = true))
        assertFalse("65536".isValidPort())

        assertTrue("4532 0151 1283 0366".isValidCreditCardLuhn())
        assertFalse("4532 0151 1283 0367".isValidCreditCardLuhn())
    }
}
