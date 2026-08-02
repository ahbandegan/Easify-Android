package ir.amirhesambandegan.easify_network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

/**
 * Executes a network request safely by catching common exceptions and mapping the response
 * to an [EasifyResult].
 *
 * @param block The suspend block containing the Ktor request (e.g., client.get("...")).
 * @return An [EasifyResult] representing the outcome of the request.
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
