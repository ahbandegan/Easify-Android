package ir.amirhesambandegan.easify_network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.util.AttributeKey
import io.ktor.http.content.TextContent

class CurlLoggerPlugin {
    class Config {
        var logTag: String = "CURL_LOG"
    }

    companion object Plugin : HttpClientPlugin<Config, CurlLoggerPlugin> {
        override val key = AttributeKey<CurlLoggerPlugin>("CurlLoggerPlugin")

        override fun prepare(block: Config.() -> Unit): CurlLoggerPlugin = CurlLoggerPlugin()

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
