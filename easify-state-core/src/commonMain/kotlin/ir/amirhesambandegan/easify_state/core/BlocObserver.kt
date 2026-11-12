package ir.amirhesambandegan.easify_state.core

import kotlin.concurrent.Volatile

/**
 * Interface for observing lifecycle, event, change, transition, and error callbacks across [Bloc] and [Cubit] instances.
 */
abstract class BlocObserver {
    open fun onCreate(bloc: Any) {}
    open fun onEvent(bloc: Bloc<*, *>, event: Any?) {}
    open fun onChange(bloc: Any, change: Change<*>) {}
    open fun onTransition(bloc: Bloc<*, *>, transition: Transition<*, *>) {}
    open fun onError(bloc: Any, error: Throwable) {}
    open fun onClose(bloc: Any) {}

    companion object {
        @Volatile
        var current: BlocObserver? = null
    }
}

/**
 * Ready-to-use multiplatform [BlocObserver] that logs state transitions, events, and errors.
 */
class LoggingBlocObserver(
    private val tag: String = "EasifyBloc",
    private val logTransitions: Boolean = true,
    private val logEvents: Boolean = true,
    private val logger: (String) -> Unit = ::println
) : BlocObserver() {

    override fun onCreate(bloc: Any) {
        logger("[$tag] onCreate(${bloc::class.simpleName})")
    }

    override fun onEvent(bloc: Bloc<*, *>, event: Any?) {
        if (logEvents) {
            logger("[$tag] onEvent(${bloc::class.simpleName}, $event)")
        }
    }

    override fun onTransition(bloc: Bloc<*, *>, transition: Transition<*, *>) {
        if (logTransitions) {
            logger("[$tag] onTransition(${bloc::class.simpleName}): ${transition.currentState} --(${transition.event})--> ${transition.nextState}")
        }
    }

    override fun onChange(bloc: Any, change: Change<*>) {
        if (bloc !is Bloc<*, *>) {
            logger("[$tag] onChange(${bloc::class.simpleName}): ${change.currentState} -> ${change.nextState}")
        }
    }

    override fun onError(bloc: Any, error: Throwable) {
        logger("[$tag] onError(${bloc::class.simpleName}): ${error.message}")
    }

    override fun onClose(bloc: Any) {
        logger("[$tag] onClose(${bloc::class.simpleName})")
    }
}
