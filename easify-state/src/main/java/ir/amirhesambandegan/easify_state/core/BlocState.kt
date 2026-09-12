package ir.amirhesambandegan.easify_state.core

/**
 * Represents the state emitted by a [Bloc] or [Cubit].
 */
interface BlocState

/**
 * Common status representation for states adhering to the Single State pattern.
 */
enum class BlocStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAILURE;

    val isInitial: Boolean get() = this == INITIAL
    val isLoading: Boolean get() = this == LOADING
    val isSuccess: Boolean get() = this == SUCCESS
    val isFailure: Boolean get() = this == FAILURE
}
