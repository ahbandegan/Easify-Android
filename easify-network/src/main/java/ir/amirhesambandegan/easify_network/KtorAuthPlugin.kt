package ir.amirhesambandegan.easify_network

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer

/**
 * An example of configuring Ktor to automatically refresh JWT tokens when receiving 401.
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
