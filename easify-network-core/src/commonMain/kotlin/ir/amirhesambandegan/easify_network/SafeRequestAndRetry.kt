package ir.amirhesambandegan.easify_network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

/**
 * Executes a Ktor HTTP request safely, propagating [CancellationException] for structured concurrency,
 * mapping 2xx responses to [EasifyResult.Success], non-2xx responses to [EasifyResult.ApiError],
 * and thrown runtime/network errors to [EasifyResult.NetworkError].
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
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        EasifyResult.NetworkError(e)
    }
}

/**
 * Executes a network call wrapped in an exponential backoff retry mechanism.
 * Retries only on [EasifyResult.NetworkError]; [EasifyResult.Success] and [EasifyResult.ApiError]
 * return immediately. Propagates coroutine cancellation cleanly.
 */
suspend fun <T> retryNetworkCall(
    maxAttempts: Int = 3,
    initialDelayMillis: Long = 1000L,
    maxDelayMillis: Long = 10000L,
    factor: Double = 2.0,
    block: suspend () -> EasifyResult<T>
): EasifyResult<T> {
    val safeMaxAttempts = maxAttempts.coerceAtLeast(1)
    var currentDelay = initialDelayMillis.coerceAtLeast(0L)
    var lastError: EasifyResult<T>? = null

    for (attempt in 1..safeMaxAttempts) {
        val result = block()

        if (result is EasifyResult.Success || result is EasifyResult.ApiError) {
            return result
        }

        lastError = result

        if (attempt != safeMaxAttempts) {
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceIn(0L, maxDelayMillis)
        }
    }

    return lastError ?: EasifyResult.NetworkError(IllegalStateException("Unknown retry failure"))
}
