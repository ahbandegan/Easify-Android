package ir.amirhesambandegan.easify_network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.content.TextContent
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import kotlinx.serialization.json.Json

internal expect fun platformDefaultHttpEngine(): HttpClientEngineFactory<*>?

/**
 * Multiplatform Ktor [HttpClient] factory with safe defaults (logging off by default to protect secrets).
 */
fun createKtorClient(
    baseUrl: String? = null,
    authToken: String? = null,
    requestTimeoutMillis: Long = 15000L,
    connectTimeoutMillis: Long = 15000L,
    socketTimeoutMillis: Long = 15000L,
    logLevel: LogLevel = LogLevel.NONE,
    jsonConfig: Json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    },
    configure: HttpClientConfig<*>.() -> Unit = {}
): HttpClient {
    val block: HttpClientConfig<*>.() -> Unit = {
        install(ContentNegotiation) {
            json(jsonConfig)
        }

        if (logLevel != LogLevel.NONE) {
            install(Logging) {
                level = logLevel
                sanitizeHeader { header ->
                    header.equals(HttpHeaders.Authorization, ignoreCase = true) ||
                        header.equals(HttpHeaders.Cookie, ignoreCase = true)
                }
            }
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

        configure()
    }

    val engine = platformDefaultHttpEngine()
    return if (engine != null) HttpClient(engine, block) else HttpClient(block)
}

/**
 * Installs Ktor [Auth] Bearer token provider and automatic 401 refresh flow.
 */
fun HttpClientConfig<*>.installAutoTokenRefresher(
    loadTokens: suspend () -> BearerTokens?,
    refreshTokens: suspend () -> BearerTokens?
) {
    install(Auth) {
        bearer {
            loadTokens { loadTokens() }
            refreshTokens { refreshTokens() }
        }
    }
}

/**
 * Multiplatform Ktor plugin logging outgoing HTTP requests as redacted cURL commands.
 */
class CurlLoggerPlugin private constructor(private val config: Config) {

    class Config {
        var logTag: String = "CURL_LOG"
        var logger: (String) -> Unit = ::println
        var redactedHeaders: Set<String> = setOf("Authorization", "Cookie", "Set-Cookie", "X-Api-Key")
        var includeBody: Boolean = true
    }

    companion object Plugin : HttpClientPlugin<Config, CurlLoggerPlugin> {
        override val key = AttributeKey<CurlLoggerPlugin>("CurlLoggerPlugin")

        override fun prepare(block: Config.() -> Unit): CurlLoggerPlugin =
            CurlLoggerPlugin(Config().apply(block))

        override fun install(plugin: CurlLoggerPlugin, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Render) {
                val request = context
                val url = request.url.buildString()
                val method = request.method.value
                val headers = request.headers.entries().joinToString(" ") { entry ->
                    val isSensitive = plugin.config.redactedHeaders.any {
                        it.equals(entry.key, ignoreCase = true)
                    }
                    val renderedValue = if (isSensitive) {
                        "<REDACTED>"
                    } else {
                        entry.value.joinToString()
                    }
                    "-H \"${entry.key}: ${renderedValue.replace("\"", "\\\"")}\""
                }

                val bodyString = if (plugin.config.includeBody && subject is TextContent) {
                    val escaped = (subject as TextContent).text.replace("'", "'\\''")
                    "-d '$escaped'"
                } else ""

                val parts = listOfNotNull(
                    "curl -X $method",
                    headers.takeIf { it.isNotEmpty() },
                    bodyString.takeIf { it.isNotEmpty() },
                    "\"$url\""
                )
                plugin.config.logger("[${plugin.config.logTag}] --> ${parts.joinToString(" ")}")

                proceed()
            }
        }
    }
}
