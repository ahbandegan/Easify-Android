package ir.amirhesambandegan.easify_network

import kotlinx.coroutines.delay

/**
 * Executes a network call wrapped in an exponential backoff retry mechanism.
 *
 * This function retries the provided [block] only when encountering network-level failures
 * ([EasifyResult.NetworkError]). Successful responses ([EasifyResult.Success]) and API-level
 * responses ([EasifyResult.ApiError], e.g. 4xx/5xx) are returned immediately without retrying.
 *
 * @param T The type of data wrapped in [EasifyResult].
 * @param maxAttempts The maximum number of attempts before returning the last error. Defaults to `3`.
 * @param initialDelayMillis The initial delay in milliseconds before the first retry attempt. Defaults to `1000L` (1 second).
 * @param maxDelayMillis The maximum delay cap in milliseconds between retry attempts. Defaults to `10000L` (10 seconds).
 * @param factor The multiplier applied to the delay after each retry. Defaults to `2.0`.
 * @param block A suspending lambda that performs the network operation and returns an [EasifyResult].
 * @return The first [EasifyResult.Success] or [EasifyResult.ApiError], or the last [EasifyResult.NetworkError] when all attempts are exhausted.
 */
suspend fun <T> retryNetworkCall(
    maxAttempts: Int = 3,
    initialDelayMillis: Long = 1000L,
    maxDelayMillis: Long = 10000L,
    factor: Double = 2.0,
    block: suspend () -> EasifyResult<T>
): EasifyResult<T> {
    var currentDelay = initialDelayMillis
    var lastError: EasifyResult<T>? = null

    for (attempt in 1..maxAttempts) {
        val result = block()

        if (result is EasifyResult.Success || result is EasifyResult.ApiError) {
            return result // Only retry on Network Errors
        }
        
        lastError = result
        
        if (attempt != maxAttempts) {
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
        }
    }
    
    return lastError ?: EasifyResult.NetworkError(Exception("Unknown retry failure"))
}
