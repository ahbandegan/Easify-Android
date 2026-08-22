package ir.amirhesambandegan.easify_network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

/**
 * Executes a network request safely by catching exceptions and mapping the HTTP response to an [EasifyResult].
 *
 * Successful responses (HTTP status in 200..299 range) are parsed into [T] and wrapped in [EasifyResult.Success].
 * Non-2xx status codes are mapped to [EasifyResult.ApiError] with the status code and description.
 * Any thrown [Exception] (e.g. timeout, no connectivity, deserialization issue) is caught and mapped to [EasifyResult.NetworkError].
 *
 * @param T The expected model type to deserialize the response body into.
 * @receiver The [HttpClient] instance used to execute the request block.
 * @param block A suspending lambda scoped to [HttpClient] returning an [HttpResponse] (e.g. `{ get("https://api.example.com/data") }`).
 * @return An [EasifyResult] holding either the deserialized body, an API error, or a network exception.
 */
suspend inline fun <reified T> HttpClient.safeRequest(
    crossinline block: suspend HttpClient.() -> HttpResponse
): EasifyResult<T> {
    return try {
        val response = block()
        if (response.status.isSuccess()) {
            EasifyResult.Success(response.body<T>())
        } else {
            EasifyResult.ApiError(response.status.value, "Server Error: ${response.status.description}")
        }
    } catch (e: Exception) {
        EasifyResult.NetworkError(e)
    }
}
