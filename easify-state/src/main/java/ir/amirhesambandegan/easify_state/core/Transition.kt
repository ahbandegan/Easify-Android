package ir.amirhesambandegan.easify_state.core

/**
 * Occurs when a new [event] is dispatched and a new [nextState] is about to be emitted by the [Bloc].
 * A [Transition] consists of the [currentState], the [event] which triggered it, and the [nextState].
 */
data class Transition<E : BlocEvent, S : BlocState>(
    val currentState: S,
    val event: E,
    val nextState: S
) {
    fun toChange(): Change<S> = Change(currentState, nextState)
}