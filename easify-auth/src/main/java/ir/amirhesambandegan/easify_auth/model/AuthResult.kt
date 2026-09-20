package ir.amirhesambandegan.easify_auth.model

sealed interface AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>
    data class Error(val throwable: Throwable) : AuthResult<Nothing>
    data object Cancelled : AuthResult<Nothing>
}