package ir.amirhesambandegan.easify_network

import kotlinx.coroutines.delay

/**
 * Wraps any EasifyResult in an Exponential Backoff retry mechanism.
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
