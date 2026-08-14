package ir.amirhesambandegan.easify_network

/**
 * A sealed interface representing the result of a network operation.
 * @param T The type of the data expected from the operation.
 */
sealed interface EasifyResult<out T> {
    /**
     * Represents a successful network operation.
     * @property data The data returned from the server.
     */
    data class Success<out T>(val data: T) : EasifyResult<T>

    /**
     * Represents an error returned by the API (e.g., 4xx or 5xx status codes).
     * @property code The HTTP status code.
     * @property message The error message from the server or a default one.
     */
    data class ApiError(val code: Int, val message: String) : EasifyResult<Nothing>

    /**
     * Represents a network-level error (e.g., no internet, timeout, serialization failure).
     * @property exception The throwable that caused the error.
     */
    data class NetworkError(val exception: Throwable) : EasifyResult<Nothing>

    /**
     * Represents the loading state of a network operation.
     */
    data object Loading : EasifyResult<Nothing>
}
