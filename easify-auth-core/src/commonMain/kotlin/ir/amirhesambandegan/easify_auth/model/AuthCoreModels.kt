package ir.amirhesambandegan.easify_auth.model

sealed interface AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>
    data class Error(val throwable: Throwable) : AuthResult<Nothing>
    data object Cancelled : AuthResult<Nothing>
}

data class AuthUser(
    val id: String,
    val email: String? = null,
    val displayName: String? = null,
    val givenName: String? = null,
    val familyName: String? = null,
    val profilePictureUrl: String? = null,
    val phoneNumber: String? = null,
    val idToken: String? = null,
    val accessToken: String? = null
)

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

sealed interface OAuthCallbackResult {
    data class Success(val code: String, val state: String?) : OAuthCallbackResult
    data class Error(val error: String, val errorDescription: String? = null) : OAuthCallbackResult
    data object Cancelled : OAuthCallbackResult
}

data class OAuthTokenResponse(
    val accessToken: String,
    val tokenType: String? = null,
    val expiresIn: Long? = null,
    val refreshToken: String? = null,
    val scope: String? = null,
    val idToken: String? = null,
    val rawResponse: String? = null
)
