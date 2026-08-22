package ir.amirhesambandegan.easify_network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Creates and configures a Ktor [HttpClient] instance powered by the [CIO] asynchronous engine with common production defaults.
 *
 * Configured plugins:
 * - [ContentNegotiation] with Kotlinx JSON serializer (lenient, ignoreUnknownKeys, prettyPrint).
 * - [Logging] with [LogLevel.ALL].
 * - [HttpTimeout] configured with request, connect, and socket timeouts.
 * - [defaultRequest] setting default base URL, optional Authorization header, Content-Type, and Accept headers.
 *
 * @param baseUrl The optional base URL prefix prepended to all request paths. Defaults to `null`.
 * @param authToken An optional Bearer authentication token included in every request's `Authorization` header.
 * @param requestTimeoutMillis The duration in milliseconds to wait for a request to complete. Defaults to `15000L` (15 seconds).
 * @param connectTimeoutMillis The duration in milliseconds to wait while establishing a connection. Defaults to `15000L` (15 seconds).
 * @param socketTimeoutMillis The duration in milliseconds to wait between data packets. Defaults to `15000L` (15 seconds).
 * @return A fully pre-configured [HttpClient] instance ready for network requests.
 */
fun createKtorClient(
    baseUrl: String? = null,
    authToken: String? = null,
    requestTimeoutMillis: Long = 15000L,
    connectTimeoutMillis: Long = 15000L,
    socketTimeoutMillis: Long = 15000L,
) = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }

    install(Logging) {
        level = LogLevel.ALL
    }

    install(HttpTimeout) {
        this.requestTimeoutMillis = requestTimeoutMillis
        this.connectTimeoutMillis = connectTimeoutMillis
        this.socketTimeoutMillis = socketTimeoutMillis
    }

    defaultRequest {
        baseUrl?.let { url(it) }
        authToken?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        header(HttpHeaders.Accept, ContentType.Application.Json)
    }
}
