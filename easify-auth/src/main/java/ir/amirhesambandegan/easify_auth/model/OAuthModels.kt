package ir.amirhesambandegan.easify_auth.model

/**
 * Standard supported OAuth providers and their default endpoints.
 */
enum class OAuthProvider(val authEndpoint: String, val tokenEndpoint: String) {
    GITHUB(
        authEndpoint = "https://github.com/login/oauth/authorize",
        tokenEndpoint = "https://github.com/login/oauth/access_token"
    ),
    APPLE(
        authEndpoint = "https://appleid.apple.com/auth/authorize",
        tokenEndpoint = "https://appleid.apple.com/auth/token"
    ),
    DISCORD(
        authEndpoint = "https://discord.com/api/oauth2/authorize",
        tokenEndpoint = "https://discord.com/api/oauth2/token"
    ),
    GOOGLE(
        authEndpoint = "https://accounts.google.com/o/oauth2/v2/auth",
        tokenEndpoint = "https://oauth2.googleapis.com/token"
    ),
    GITLAB(
        authEndpoint = "https://gitlab.com/oauth/authorize",
        tokenEndpoint = "https://gitlab.com/oauth/token"
    ),
    MICROSOFT(
        authEndpoint = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize",
        tokenEndpoint = "https://login.microsoftonline.com/common/oauth2/v2.0/token"
    ),
    SPOTIFY(
        authEndpoint = "https://accounts.spotify.com/authorize",
        tokenEndpoint = "https://accounts.spotify.com/api/token"
    ),
    LINKEDIN(
        authEndpoint = "https://www.linkedin.com/oauth/v2/authorization",
        tokenEndpoint = "https://www.linkedin.com/oauth/v2/accessToken"
    ),
    TWITCH(
        authEndpoint = "https://id.twitch.tv/oauth2/authorize",
        tokenEndpoint = "https://id.twitch.tv/oauth2/token"
    ),
    X(
        authEndpoint = "https://twitter.com/i/oauth2/authorize",
        tokenEndpoint = "https://api.twitter.com/2/oauth2/token"
    ),
    CUSTOM("", "")
}

/**
 * Configuration options for launching an OAuth authorization flow.
 *
 * @property clientId The registered client identifier.
 * @property redirectUri The registered redirect URI for handling callback.
 * @property scopes The list of permissions/scopes requested.
 * @property clientSecret Optional client secret (for token exchange in backend-for-frontend scenarios).
 * @property customAuthEndpoint Custom authorization endpoint if using [OAuthProvider.CUSTOM].
 * @property customTokenEndpoint Custom token exchange endpoint if using [OAuthProvider.CUSTOM].
 * @property usePkce Whether to enable PKCE (Proof Key for Code Exchange, RFC 7636). Highly recommended for mobile.
 * @property state Optional custom state token to protect against CSRF attacks. If null, a secure random one is generated.
 * @property additionalParameters Any additional query parameters to include in the authorization request.
 */
data class OAuthConfig(
    val clientId: String,
    val redirectUri: String,
    val scopes: List<String> = emptyList(),
    val clientSecret: String? = null,
    val customAuthEndpoint: String? = null,
    val customTokenEndpoint: String? = null,
    val usePkce: Boolean = true,
    val state: String? = null,
    val additionalParameters: Map<String, String> = emptyMap()
)

/**
 * Internal callback result emitted by OAuthCallbackActivity.
 */
sealed interface OAuthCallbackResult {
    data class Success(val code: String, val state: String?) : OAuthCallbackResult
    data class Error(val error: String, val errorDescription: String? = null) : OAuthCallbackResult
    data object Cancelled : OAuthCallbackResult
}

/**
 * Response received when exchanging an authorization code for an OAuth access token.
 */
data class OAuthTokenResponse(
    val accessToken: String,
    val tokenType: String? = null,
    val expiresIn: Long? = null,
    val refreshToken: String? = null,
    val scope: String? = null,
    val idToken: String? = null,
    val rawResponse: String? = null
)