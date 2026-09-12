package ir.amirhesambandegan.easify_state.core

/**
 * An interface for observing the behavior of [Bloc] and [Cubit] instances.
 */
abstract class BlocObserver {
    /**
     * Called whenever a [Bloc] or [Cubit] is instantiated.
     */
    open fun onCreate(bloc: Any) {}

    /**
     * Called whenever an [event] is added to any [bloc].
     */
    open fun onEvent(bloc: Bloc<*, *>, event: Any?) {}

    /**
     * Called whenever a [change] occurs in any [bloc] or [Cubit].
     */
    open fun onChange(bloc: Any, change: Change<*>) {}

    /**
     * Called whenever a [transition] occurs in any [bloc].
     */
    open fun onTransition(bloc: Bloc<*, *>, transition: Transition<*, *>) {}

    /**
     * Called whenever an [error] is thrown in any [bloc] or [Cubit].
     */
    open fun onError(bloc: Any, error: Throwable) {}

    /**
     * Called whenever a [bloc] or [Cubit] is closed.
     */
    open fun onClose(bloc: Any) {}

    companion object {
        var current: BlocObserver? = null
    }
}
