package ir.amirhesambandegan.easify_network

/**
 * A sealed interface representing the result of a network operation.
 *
 * Encapsulates success, API error, network failure, and loading states.
 *
 * @param T The type of data expected from the operation.
 */
sealed interface EasifyResult<out T> {
    /**
     * Represents a successful network operation.
     *
     * @param T The type of data returned.
     * @property data The payload returned from the server.
     */
    data class Success<out T>(val data: T) : EasifyResult<T>

    /**
     * Represents an HTTP error returned by the API (e.g., 4xx or 5xx status codes).
     *
     * @property code The HTTP status code returned by the server.
     * @property message The error message describing the failure.
     */
    data class ApiError(val code: Int, val message: String) : EasifyResult<Nothing>

    /**
     * Represents a network-level error (e.g., no internet connectivity, timeout, or serialization failure).
     *
     * @property exception The underlying [Throwable] that caused the network failure.
     */
    data class NetworkError(val exception: Throwable) : EasifyResult<Nothing>

    /**
     * Represents the in-progress / loading state of an ongoing network operation.
     */
    data object Loading : EasifyResult<Nothing>
}
