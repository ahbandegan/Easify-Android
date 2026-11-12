package ir.amirhesambandegan.easify_auth

import ir.amirhesambandegan.easify_auth.oauth.PkceHelper
import ir.amirhesambandegan.easify_auth.sms.OtpExtractor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthCoreTest {

    @Test
    fun rfc7636AppendixBVectorMatches() {
        val verifier = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk"
        val expectedChallenge = "E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM"
        assertEquals(expectedChallenge, PkceHelper.generateCodeChallenge(verifier))
    }

    @Test
    fun pkceAndStateHelpersWork() {
        val verifier = PkceHelper.generateCodeVerifier()
        assertTrue(verifier.length in 43..128)
        assertTrue(Regex("^[A-Za-z0-9_-]+$").matches(verifier))

        val state = PkceHelper.generateState()
        assertTrue(PkceHelper.verifyState(state, state))
        assertFalse(PkceHelper.verifyState(state, state.dropLast(1) + "X"))
        assertFalse(PkceHelper.verifyState(state, null))
    }

    @Test
    fun otpExtractorHandlesPersianDigits() {
        assertEquals("482910", OtpExtractor.extractOtp("کد تایید شما: ۴۸۲۹۱۰ است"))
    }
}
