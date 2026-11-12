package ir.amirhesambandegan.easify_auth

import ir.amirhesambandegan.easify_auth.oauth.PkceHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PkceHelperTest {

    @Test
    fun testRfc7636AppendixBVector() {
        // Test vector from RFC 7636 Appendix B:
        // code_verifier = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk"
        // code_challenge = "E9Melhoa2OwvFrGMTJguCH5rtG64DTbTZMxa0jGwUGU"
        val verifier = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk"
        val expectedChallenge = "E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM"

        val challenge = PkceHelper.generateCodeChallenge(verifier)
        assertEquals(expectedChallenge, challenge)
    }

    @Test
    fun testGeneratedVerifierMeetsRfcRequirements() {
        val verifier = PkceHelper.generateCodeVerifier(32)
        // Must be between 43 and 128 chars
        assertTrue("Verifier length should be between 43 and 128", verifier.length in 43..128)

        // Must only contain URL-safe chars (A-Z, a-z, 0-9, -, _, ~, .)
        val urlSafePattern = Regex("^[A-Za-z0-9_-]+$")
        assertTrue("Verifier should be URL-safe string", urlSafePattern.matches(verifier))
    }

    @Test
    fun testGeneratedStateIsUrlSafe() {
        val state = PkceHelper.generateState(16)
        assertTrue(state.isNotEmpty())
        val urlSafePattern = Regex("^[A-Za-z0-9_-]+$")
        assertTrue("State should be URL-safe string", urlSafePattern.matches(state))
    }
}
