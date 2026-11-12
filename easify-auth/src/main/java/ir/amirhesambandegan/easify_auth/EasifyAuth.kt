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
import ir.amirhesambandegan.easify_auth.oauth.PkceHelper
import ir.amirhesambandegan.easify_auth.sms.AppSignatureHelper
import ir.amirhesambandegan.easify_auth.sms.SmsRetrieverHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Main entry point for authentication in Easify Android.
 * Provides unified APIs for Google Sign-In, modern OAuth 2.0 / 2.1 (with PKCE & CSRF state validation),
 * Password Manager, and permission-protected SMS OTP Retriever / User Consent.
 */
class EasifyAuth(private val context: Context) {
    private val appContext = context.applicationContext
    private val googleAuthHelper by lazy { GoogleAuthHelper(context) }
    private val oAuthHelper by lazy { OAuthHelper(context) }
    private val smsHelper by lazy { SmsRetrieverHelper(appContext) }

    // region Google & Credential Manager

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

    suspend fun signOut(): AuthResult<Unit> {
        return googleAuthHelper.signOut()
    }

    suspend fun savePassword(username: String, password: String): AuthResult<Unit> {
        return googleAuthHelper.savePassword(username, password)
    }

    suspend fun getSavedPassword(): AuthResult<Pair<String, String>> {
        return googleAuthHelper.getSavedPassword()
    }

    // endregion

    // region OAuth 2.0 / 2.1

    /**
     * Launches an OAuth authorization flow in Chrome Custom Tabs with PKCE and constant-time
     * CSRF state validation.
     */
    suspend fun signInWithOAuth(
        provider: OAuthProvider,
        config: OAuthConfig
    ): AuthResult<String> {
        return try {
            oAuthHelper.launchOAuth(provider, config)
            when (val callbackResult = OAuthCallbackActivity.callbackResultFlow.first()) {
                is OAuthCallbackResult.Success -> {
                    if (!PkceHelper.verifyState(oAuthHelper.lastState, callbackResult.state)) {
                        AuthResult.Error(SecurityException("OAuth CSRF state validation failed"))
                    } else {
                        AuthResult.Success(callbackResult.code)
                    }
                }
                is OAuthCallbackResult.Error -> {
                    AuthResult.Error(
                        IllegalStateException("OAuth error: ${callbackResult.error} (${callbackResult.errorDescription})")
                    )
                }
                is OAuthCallbackResult.Cancelled -> AuthResult.Cancelled
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }

    /**
     * Launches an OAuth authorization flow, validates the CSRF state, and exchanges the
     * authorization code for an OAuth access token using PKCE.
     */
    suspend fun signInWithOAuthAndExchange(
        provider: OAuthProvider,
        config: OAuthConfig
    ): AuthResult<OAuthTokenResponse> {
        return try {
            val verifier = oAuthHelper.launchOAuth(provider, config)
            when (val callbackResult = OAuthCallbackActivity.callbackResultFlow.first()) {
                is OAuthCallbackResult.Success -> {
                    if (!PkceHelper.verifyState(oAuthHelper.lastState, callbackResult.state)) {
                        AuthResult.Error(SecurityException("OAuth CSRF state validation failed"))
                    } else {
                        oAuthHelper.exchangeCodeForToken(
                            provider = provider,
                            config = config,
                            code = callbackResult.code,
                            codeVerifier = verifier
                        )
                    }
                }
                is OAuthCallbackResult.Error -> {
                    AuthResult.Error(
                        IllegalStateException("OAuth error: ${callbackResult.error} (${callbackResult.errorDescription})")
                    )
                }
                is OAuthCallbackResult.Cancelled -> AuthResult.Cancelled
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }

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

    fun listenForSmsOtp(
        otpLengthRange: IntRange = 4..8
    ): Flow<AuthResult<String>> = smsHelper.listenForOtp(otpLengthRange)

    fun startSmsRetriever(
        onReady: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        smsHelper.startListening(onReady, onError)
    }

    fun listenForSmsUserConsent(
        senderPhoneNumber: String? = null
    ): Flow<AuthResult<Intent>> = smsHelper.listenForUserConsentIntent(senderPhoneNumber)

    fun startSmsUserConsent(
        senderPhoneNumber: String? = null,
        onReady: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        smsHelper.startUserConsent(senderPhoneNumber, onReady, onError)
    }

    fun getAppSignatures(): List<String> = AppSignatureHelper.getAppSignatures(appContext)

    // endregion
}
