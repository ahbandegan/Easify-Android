package ir.amirhesambandegan.easify_auth

import android.content.Context
import android.content.Intent
import ir.amirhesambandegan.easify_auth.google.GoogleAuthHelper
import ir.amirhesambandegan.easify_auth.model.AuthResult
import ir.amirhesambandegan.easify_auth.model.AuthUser
import ir.amirhesambandegan.easify_auth.model.OAuthCallbackResult
import ir.amirhesambandegan.easify_auth.model.OAuthConfig
import ir.amirhesambandegan.easify_auth.model.OAuthProvider
import ir.amirhesambandegan.easify_auth.model.OAuthTokenResponse
import ir.amirhesambandegan.easify_auth.oauth.OAuthCallbackActivity
import ir.amirhesambandegan.easify_auth.oauth.OAuthHelper
import ir.amirhesambandegan.easify_auth.sms.AppSignatureHelper
import ir.amirhesambandegan.easify_auth.sms.SmsRetrieverHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Main entry point for authentication in Easify Android.
 * Provides unified APIs for Google Sign-In, modern OAuth 2.0 / 2.1 (with PKCE),
 * Password Manager, and SMS OTP Retriever / User Consent.
 */
class EasifyAuth(private val context: Context) {
    private val googleAuthHelper by lazy { GoogleAuthHelper(context) }
    private val oAuthHelper by lazy { OAuthHelper(context) }
    private val smsHelper by lazy { SmsRetrieverHelper(context) }

    // region Google & Credential Manager

    /**
     * Signs in with Google using Android's Credential Manager.
     *
     * @param serverClientId Web Client ID from Google Cloud / Firebase Console.
     * @param filterByAuthorizedAccounts Only display accounts previously authorized for this app.
     * @param autoSelectEnabled Automatically sign in if a single authorized account exists.
     * @param nonce Optional cryptographic nonce.
     */
    suspend fun signInWithGoogle(
        serverClientId: String,
        filterByAuthorizedAccounts: Boolean = false,
        autoSelectEnabled: Boolean = false,
        nonce: String? = null
    ): AuthResult<AuthUser> {
        return googleAuthHelper.signIn(
            serverClientId = serverClientId,
            filterByAuthorizedAccounts = filterByAuthorizedAccounts,
            autoSelectEnabled = autoSelectEnabled,
            nonce = nonce
        )
    }

    /**
     * Clears credentials and signs the user out from Credential Manager.
     */
    suspend fun signOut(): AuthResult<Unit> {
        return googleAuthHelper.signOut()
    }

    /**
     * Saves username/password credentials into Google Password Manager.
     */
    suspend fun savePassword(username: String, password: String): AuthResult<Unit> {
        return googleAuthHelper.savePassword(username, password)
    }

    /**
     * Retrieves saved username/password credentials from Credential Manager.
     */
    suspend fun getSavedPassword(): AuthResult<Pair<String, String>> {
        return googleAuthHelper.getSavedPassword()
    }

    // endregion

    // region OAuth 2.0 / 2.1

    /**
     * Launches an OAuth authorization flow in Chrome Custom Tabs with optional PKCE and CSRF state.
     * Suspends until the user completes authorization, returning the authorization code.
     */
    suspend fun signInWithOAuth(
        provider: OAuthProvider,
        config: OAuthConfig
    ): AuthResult<String> {
        return try {
            oAuthHelper.launchOAuth(provider, config)
            when (val callbackResult = OAuthCallbackActivity.callbackResultFlow.first()) {
                is OAuthCallbackResult.Success -> {
                    AuthResult.Success(callbackResult.code)
                }
                is OAuthCallbackResult.Error -> {
                    AuthResult.Error(
                        IllegalStateException("OAuth error: ${callbackResult.error} (${callbackResult.errorDescription})")
                    )
                }
                is OAuthCallbackResult.Cancelled -> {
                    AuthResult.Cancelled
                }
            }
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }

    /**
     * Launches an OAuth authorization flow and automatically exchanges the authorization code
     * for an OAuth access token (and optional refresh token / id token) using PKCE.
     */
    suspend fun signInWithOAuthAndExchange(
        provider: OAuthProvider,
        config: OAuthConfig
    ): AuthResult<OAuthTokenResponse> {
        return try {
            val verifier = oAuthHelper.launchOAuth(provider, config)
            when (val callbackResult = OAuthCallbackActivity.callbackResultFlow.first()) {
                is OAuthCallbackResult.Success -> {
                    oAuthHelper.exchangeCodeForToken(
                        provider = provider,
                        config = config,
                        code = callbackResult.code,
                        codeVerifier = verifier
                    )
                }
                is OAuthCallbackResult.Error -> {
                    AuthResult.Error(
                        IllegalStateException("OAuth error: ${callbackResult.error} (${callbackResult.errorDescription})")
                    )
                }
                is OAuthCallbackResult.Cancelled -> {
                    AuthResult.Cancelled
                }
            }
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }

    /**
     * Exchanges an authorization code for an OAuth access token at the provider's token endpoint.
     */
    suspend fun exchangeOAuthCodeForToken(
        provider: OAuthProvider,
        config: OAuthConfig,
        code: String,
        codeVerifier: String? = oAuthHelper.lastCodeVerifier
    ): AuthResult<OAuthTokenResponse> {
        return oAuthHelper.exchangeCodeForToken(
            provider = provider,
            config = config,
            code = code,
            codeVerifier = codeVerifier
        )
    }

    // endregion

    // region SMS Retriever & OTP

    /**
     * Listens for an incoming SMS containing an OTP code via Google Play SMS Retriever API.
     * Automatically registers and cleans up the BroadcastReceiver.
     *
     * @param otpLengthRange The acceptable number of digits in the OTP code (defaults to 4..8 digits).
     */
    fun listenForSmsOtp(
        otpLengthRange: IntRange = 4..8
    ): Flow<AuthResult<String>> {
        return smsHelper.listenForOtp(otpLengthRange)
    }

    /**
     * Starts listening for SMS Retriever in Google Play Services.
     */
    fun startSmsRetriever(
        onReady: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        smsHelper.startListening(onReady, onError)
    }

    /**
     * Starts SMS User Consent API. This does NOT require the 11-character hash in the SMS text.
     * Emits a consent Intent when an SMS arrives, which can be passed to an ActivityResultLauncher.
     */
    fun listenForSmsUserConsent(
        senderPhoneNumber: String? = null
    ): Flow<AuthResult<Intent>> {
        return smsHelper.listenForUserConsentIntent(senderPhoneNumber)
    }

    /**
     * Starts SMS User Consent client in Google Play Services.
     */
    fun startSmsUserConsent(
        senderPhoneNumber: String? = null,
        onReady: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        smsHelper.startUserConsent(senderPhoneNumber, onReady, onError)
    }

    /**
     * Returns the 11-character application signature hash codes required by SMS Retriever API.
     * Share this code with your SMS gateway / backend to append at the end of OTP messages.
     */
    fun getAppSignatures(): List<String> {
        return AppSignatureHelper.getAppSignatures(context)
    }

    // endregion
}