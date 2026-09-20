package ir.amirhesambandegan.easify_auth.oauth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import ir.amirhesambandegan.easify_auth.model.OAuthCallbackResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Trampoline activity that catches the OAuth deep link redirect from the browser.
 * Extracts the authorization code, state, or error parameters and finishes immediately.
 */
class OAuthCallbackActivity : Activity() {

    companion object {
        private val _callbackResultFlow = MutableSharedFlow<OAuthCallbackResult>(extraBufferCapacity = 1)
        val callbackResultFlow = _callbackResultFlow.asSharedFlow()

        private val _authCodeFlow = MutableSharedFlow<String?>(extraBufferCapacity = 1)
        @Deprecated("Use callbackResultFlow for full error and state support")
        val authCodeFlow = _authCodeFlow.asSharedFlow()

        /**
         * Emits a cancelled state (for instance, when the user dismisses the Custom Tab).
         */
        internal fun emitCancelled() {
            _callbackResultFlow.tryEmit(OAuthCallbackResult.Cancelled)
            _authCodeFlow.tryEmit(null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        finish()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
        finish()
    }

    private fun handleIntent(intent: Intent?) {
        val uri = intent?.data
        if (uri == null) {
            _callbackResultFlow.tryEmit(OAuthCallbackResult.Cancelled)
            _authCodeFlow.tryEmit(null)
            return
        }

        val code = uri.getQueryParameter("code")
        val state = uri.getQueryParameter("state")
        val error = uri.getQueryParameter("error")
        val errorDesc = uri.getQueryParameter("error_description") ?: uri.getQueryParameter("error_message")

        when {
            code != null -> {
                _callbackResultFlow.tryEmit(OAuthCallbackResult.Success(code = code, state = state))
                _authCodeFlow.tryEmit(code)
            }
            error != null -> {
                _callbackResultFlow.tryEmit(OAuthCallbackResult.Error(error = error, errorDescription = errorDesc))
                _authCodeFlow.tryEmit(null)
            }
            else -> {
                _callbackResultFlow.tryEmit(OAuthCallbackResult.Cancelled)
                _authCodeFlow.tryEmit(null)
            }
        }
    }
}