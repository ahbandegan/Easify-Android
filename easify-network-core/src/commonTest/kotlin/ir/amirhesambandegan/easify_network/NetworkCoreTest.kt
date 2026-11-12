package ir.amirhesambandegan.easify_network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class NetworkCoreTest {

    @Test
    fun safeRequestMapsSuccessAndApiErrorAndPreservesCancellation() = runTest {
        val mockClient = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    when (request.url.encodedPath) {
                        "/ok" -> respond("{\"status\":\"ok\"}", HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
                        "/fail" -> respond("Unauthorized", HttpStatusCode.Unauthorized)
                        else -> respond("boom", HttpStatusCode.InternalServerError)
                    }
                }
            }
        }

        val ok = mockClient.safeRequest<String> { get("https://easify.test/ok") }
        assertTrue(ok.isSuccess)
        assertEquals("{\"status\":\"ok\"}", ok.getOrNull())

        val unauthorized = mockClient.safeRequest<String> { get("https://easify.test/fail") }
        assertTrue(unauthorized is EasifyResult.ApiError)
        assertEquals(401, unauthorized.code)

        assertFailsWith<CancellationException> {
            mockClient.safeRequest<String> { throw CancellationException("cancelled") }
        }
    }

    @Test
    fun curlLoggerRedactsSensitiveHeadersAndStreamsDownload() = runTest {
        val logged = mutableListOf<String>()
        val payload = "hello easify streaming download".encodeToByteArray()

        val client = HttpClient(MockEngine) {
            install(CurlLoggerPlugin) {
                logTag = "TEST_CURL"
                logger = { logged += it }
            }
            engine {
                addHandler { request ->
                    respond(
                        content = payload,
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentLength, payload.size.toString())
                    )
                }
            }
        }

        val buffer = Buffer()
        val progress = client.downloadToSink("https://easify.test/file.bin", buffer).toList()
        assertEquals(100, progress.last())
        assertEquals(payload.decodeToString(), buffer.readByteArray().decodeToString())

        // Trigger request with Authorization header to test redaction
        client.safeRequest<String> {
            get("https://easify.test/secure") {
                headers.append(HttpHeaders.Authorization, "Bearer super_secret_token")
            }
        }
        assertTrue(logged.any { it.contains("<REDACTED>") && !it.contains("super_secret_token") })
    }
}
