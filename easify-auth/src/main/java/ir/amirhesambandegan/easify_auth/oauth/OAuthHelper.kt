package ir.amirhesambandegan.easify_auth.oauth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import ir.amirhesambandegan.easify_auth.model.AuthResult
import ir.amirhesambandegan.easify_auth.model.OAuthConfig
import ir.amirhesambandegan.easify_auth.model.OAuthProvider
import ir.amirhesambandegan.easify_auth.model.OAuthTokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

internal class OAuthHelper(private val context: Context) {

    @Volatile
    var lastCodeVerifier: String? = null
        private set

    @Volatile
    var lastState: String? = null
        private set

    fun launchOAuth(
        provider: OAuthProvider,
        config: OAuthConfig
    ): String? {
        val endpoint = if (provider == OAuthProvider.CUSTOM) {
            config.customAuthEndpoint
                ?: throw IllegalArgumentException("customAuthEndpoint must not be null for CUSTOM provider")
        } else {
            provider.authEndpoint
        }

        val state = config.state ?: PkceHelper.generateState()
        lastState = state

        val uriBuilder = endpoint.toUri().buildUpon()
            .appendQueryParameter("client_id", config.clientId)
            .appendQueryParameter("redirect_uri", config.redirectUri)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("state", state)

        if (config.scopes.isNotEmpty()) {
            uriBuilder.appendQueryParameter("scope", config.scopes.joinToString(" "))
        }

        var verifier: String? = null
        if (config.usePkce) {
            verifier = PkceHelper.generateCodeVerifier()
            lastCodeVerifier = verifier
            val challenge = PkceHelper.generateCodeChallenge(verifier)
            uriBuilder.appendQueryParameter("code_challenge", challenge)
            uriBuilder.appendQueryParameter("code_challenge_method", "S256")
        } else {
            lastCodeVerifier = null
        }

        for ((key, value) in config.additionalParameters) {
            uriBuilder.appendQueryParameter(key, value)
        }

        val authUri = uriBuilder.build()

        val customTabsIntent = CustomTabsIntent.Builder().build()
        if (context !is Activity) {
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        customTabsIntent.launchUrl(context, authUri)

        return verifier
    }

    suspend fun exchangeCodeForToken(
        provider: OAuthProvider,
        config: OAuthConfig,
        code: String,
        codeVerifier: String? = lastCodeVerifier
    ): AuthResult<OAuthTokenResponse> = withContext(Dispatchers.IO) {
        val tokenEndpoint = if (provider == OAuthProvider.CUSTOM) {
            config.customTokenEndpoint
                ?: return@withContext AuthResult.Error(
                    IllegalArgumentException("customTokenEndpoint must not be null for CUSTOM provider")
                )
        } else {
            provider.tokenEndpoint
        }

        try {
            val url = URL(tokenEndpoint)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.doInput = true
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

            val params = mutableMapOf(
                "grant_type" to "authorization_code",
                "code" to code,
                "redirect_uri" to config.redirectUri,
                "client_id" to config.clientId
            )

            config.clientSecret?.let {
                params["client_secret"] = it
            }

            if (codeVerifier != null) {
                params["code_verifier"] = codeVerifier
            }

            val postData = params.entries.joinToString("&") { (key, value) ->
                "${URLEncoder.encode(key, "UTF-8")}=${URLEncoder.encode(value, "UTF-8")}"
            }

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(postData)
                writer.flush()
            }

            val responseCode = connection.responseCode
            val inputStream = if (responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream ?: connection.inputStream
            }

            val rawResponse = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }

            if (responseCode !in 200..299) {
                return@withContext AuthResult.Error(
                    IllegalStateException("HTTP $responseCode from $tokenEndpoint: $rawResponse")
                )
            }

            val json = JSONObject(rawResponse)
            if (json.has("error")) {
                val error = json.getString("error")
                val desc = if (json.has("error_description")) json.getString("error_description") else null
                return@withContext AuthResult.Error(
                    IllegalStateException("OAuth token error: $error ($desc)")
                )
            }

            val tokenResponse = OAuthTokenResponse(
                accessToken = json.getString("access_token"),
                tokenType = if (json.has("token_type")) json.getString("token_type") else null,
                expiresIn = if (json.has("expires_in")) json.getLong("expires_in") else null,
                refreshToken = if (json.has("refresh_token")) json.getString("refresh_token") else null,
                scope = if (json.has("scope")) json.getString("scope") else null,
                idToken = if (json.has("id_token")) json.getString("id_token") else null,
                rawResponse = rawResponse
            )

            AuthResult.Success(tokenResponse)
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }
}