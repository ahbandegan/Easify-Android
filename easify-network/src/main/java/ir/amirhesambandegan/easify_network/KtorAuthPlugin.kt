package ir.amirhesambandegan.easify_network

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer

/**
 * Installs and configures Ktor's [Auth] plugin for automatic Bearer token management and token refresh.
 *
 * Intercepts unauthorized HTTP responses (401 Unauthorized) to automatically execute the [refreshTokens]
 * lambda and obtain updated [BearerTokens], preventing manual authentication refresh handling across calls.
 *
 * @receiver The [HttpClientConfig] to install the authentication plugin on.
 * @param loadTokens A suspending lambda returning the currently persisted [BearerTokens], or `null` if none exist.
 * @param refreshTokens A suspending lambda executed when a 401 response is received to refresh tokens and return the updated [BearerTokens], or `null` on failure.
 */
fun HttpClientConfig<*>.installAutoTokenRefresher(
    loadTokens: suspend () -> BearerTokens?,
    refreshTokens: suspend () -> BearerTokens?
) {
    install(Auth) {
        bearer {
            loadTokens {
                loadTokens()
            }
            refreshTokens {
                // When 401 is encountered, this block is called.
                // Call your refresh API here and return the new BearerTokens.
                refreshTokens()
            }
        }
    }
}
