package ir.amirhesambandegan.easify_network

/**
 * A sealed interface representing the outcome of a network operation.
 */
sealed interface EasifyResult<out T> {
    data class Success<out T>(val data: T) : EasifyResult<T>
    data class ApiError(val code: Int, val message: String) : EasifyResult<Nothing>
    data class NetworkError(val exception: Throwable) : EasifyResult<Nothing>
    data object Loading : EasifyResult<Nothing>
}

val EasifyResult<*>.isSuccess: Boolean get() = this is EasifyResult.Success
val EasifyResult<*>.isError: Boolean get() = this is EasifyResult.ApiError || this is EasifyResult.NetworkError

fun <T> EasifyResult<T>.getOrNull(): T? = (this as? EasifyResult.Success)?.data

fun <T> EasifyResult<T>.getOrDefault(defaultValue: T): T = (this as? EasifyResult.Success)?.data ?: defaultValue

inline fun <T, R> EasifyResult<T>.map(transform: (T) -> R): EasifyResult<R> = when (this) {
    is EasifyResult.Success -> EasifyResult.Success(transform(data))
    is EasifyResult.ApiError -> this
    is EasifyResult.NetworkError -> this
    is EasifyResult.Loading -> this
}

inline fun <T> EasifyResult<T>.onSuccess(action: (T) -> Unit): EasifyResult<T> {
    if (this is EasifyResult.Success) action(data)
    return this
}

inline fun <T> EasifyResult<T>.onApiError(action: (code: Int, message: String) -> Unit): EasifyResult<T> {
    if (this is EasifyResult.ApiError) action(code, message)
    return this
}

inline fun <T> EasifyResult<T>.onNetworkError(action: (Throwable) -> Unit): EasifyResult<T> {
    if (this is EasifyResult.NetworkError) action(exception)
    return this
}

inline fun <T> EasifyResult<T>.onError(action: (message: String, cause: Throwable?) -> Unit): EasifyResult<T> {
    when (this) {
        is EasifyResult.ApiError -> action(message, null)
        is EasifyResult.NetworkError -> action(exception.message ?: "Network error", exception)
        else -> Unit
    }
    return this
}

inline fun <T> EasifyResult<T>.onLoading(action: () -> Unit): EasifyResult<T> {
    if (this is EasifyResult.Loading) action()
    return this
}
