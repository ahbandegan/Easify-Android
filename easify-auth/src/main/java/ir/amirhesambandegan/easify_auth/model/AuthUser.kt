package ir.amirhesambandegan.easify_auth.model

/**
 * Represents an authenticated user profile.
 *
 * @property id Unique user identifier or email.
 * @property email User's email address if available.
 * @property displayName User's full display name.
 * @property givenName User's given (first) name if available.
 * @property familyName User's family (last) name if available.
 * @property profilePictureUrl URL to the user's avatar/profile picture.
 * @property phoneNumber User's phone number if available.
 * @property idToken OpenID Connect ID Token (JWT) issued by provider.
 * @property accessToken OAuth Access Token if available.
 */
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