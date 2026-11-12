package ir.amirhesambandegan.easify_auth

import ir.amirhesambandegan.easify_auth.model.OAuthConfig
import ir.amirhesambandegan.easify_auth.model.OAuthProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OAuthModelsTest {

    @Test
    fun testOAuthProvidersEndpoints() {
        assertTrue(OAuthProvider.GITHUB.authEndpoint.contains("github.com"))
        assertTrue(OAuthProvider.GOOGLE.authEndpoint.contains("google.com"))
        assertTrue(OAuthProvider.DISCORD.authEndpoint.contains("discord.com"))
        assertTrue(OAuthProvider.APPLE.authEndpoint.contains("apple.com"))
        assertTrue(OAuthProvider.GITLAB.authEndpoint.contains("gitlab.com"))
        assertTrue(OAuthProvider.MICROSOFT.authEndpoint.contains("microsoftonline.com"))
        assertTrue(OAuthProvider.SPOTIFY.authEndpoint.contains("spotify.com"))
        assertTrue(OAuthProvider.LINKEDIN.authEndpoint.contains("linkedin.com"))
        assertTrue(OAuthProvider.TWITCH.authEndpoint.contains("twitch.tv"))
        assertTrue(OAuthProvider.X.authEndpoint.contains("twitter.com"))
    }

    @Test
    fun testOAuthConfigDefaults() {
        val config = OAuthConfig(
            clientId = "test-client-id",
            redirectUri = "myapp://oauth/callback"
        )

        assertEquals("test-client-id", config.clientId)
        assertEquals("myapp://oauth/callback", config.redirectUri)
        assertTrue(config.usePkce)
        assertTrue(config.scopes.isEmpty())
    }
}
