package ir.amirhesambandegan.easify_network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.util.AttributeKey
import io.ktor.http.content.TextContent

/**
 * A Ktor [HttpClient] plugin that logs outgoing HTTP requests as equivalent cURL commands.
 *
 * This simplifies debugging network requests by providing ready-to-use cURL commands in Logcat.
 */
class CurlLoggerPlugin {

    /**
     * Configuration options for [CurlLoggerPlugin].
     */
    class Config {
        /**
         * The log tag used when writing cURL commands to Android [Log].
         * Defaults to `"CURL_LOG"`.
         */
        var logTag: String = "CURL_LOG"
    }

    /**
     * Companion plugin object implementing [HttpClientPlugin] for installation and configuration into [HttpClient].
     */
    companion object Plugin : HttpClientPlugin<Config, CurlLoggerPlugin> {
        /**
         * The unique key identifying this plugin within the Ktor plugin registry.
         */
        override val key = AttributeKey<CurlLoggerPlugin>("CurlLoggerPlugin")

        /**
         * Prepares a new instance of [CurlLoggerPlugin] by executing the configuration block.
         *
         * @param block Configuration lambda.
         * @return A configured instance of [CurlLoggerPlugin].
         */
        override fun prepare(block: Config.() -> Unit): CurlLoggerPlugin = CurlLoggerPlugin()

        /**
         * Installs the plugin into the given [HttpClient] scope and intercepts outgoing HTTP requests.
         *
         * @param plugin The [CurlLoggerPlugin] instance being installed.
         * @param scope The [HttpClient] to install the plugin into.
         */
        override fun install(plugin: CurlLoggerPlugin, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                val request = context
                val url = request.url.toString()
                val method = request.method.value
                val headers = request.headers.entries().joinToString(" ") { 
                    "-H \"${it.key}: ${it.value.joinToString()}\"" 
                }
                
                val body = request.body
                val bodyString = if (body is TextContent) {
                    "-d '${body.text}'"
                } else ""
                
                val curlCommand = "curl -X $method $headers $bodyString \"$url\""
                Log.d("CURL_LOG", "--> $curlCommand")
                
                proceed()
            }
        }
    }
}
