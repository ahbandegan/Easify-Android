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
 * Creates and configures a Ktor [HttpClient] with common defaults.
 *
 * @param baseUrl The base URL for requests. Defaults to null.
 * @param authToken An optional Bearer token to be added to every request header.
 * @param requestTimeoutMillis The timeout for a request to complete. Defaults to 15000ms.
 * @param connectTimeoutMillis The timeout for establishing a connection. Defaults to 15000ms.
 * @param socketTimeoutMillis The timeout for receiving data. Defaults to 15000ms.
 * @return A configured [HttpClient] instance.
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
