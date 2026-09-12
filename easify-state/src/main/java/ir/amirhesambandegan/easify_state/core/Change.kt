package ir.amirhesambandegan.easify_state.core

/**
 * Represents a state change in a [Bloc] or [Cubit].
 * A [Change] consists of the [currentState] and [nextState].
 */
data class Change<S : BlocState>(
    val currentState: S,
    val nextState: S
)
