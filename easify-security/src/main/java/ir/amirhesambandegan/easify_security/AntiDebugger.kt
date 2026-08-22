package ir.amirhesambandegan.easify_security

import android.os.Debug
import kotlinx.coroutines.*

/**
 * A utility object designed to detect and prevent debugging attempts on the application.
 */
object AntiDebugger {

    /**
     * The coroutine job responsible for running the periodic debugger checks.
     */
    private var job: Job? = null

    /**
     * Periodically checks if a debugger is attached. If so, immediately kills the app
     * to prevent reverse engineering and memory dumping.
     *
     * @param scope The coroutine scope in which the monitoring job will be launched.
     * Defaults to a scope using [Dispatchers.Default].
     */
    fun startMonitoring(scope: CoroutineScope = CoroutineScope(Dispatchers.Default)) {
        if (job?.isActive == true) return
        
        job = scope.launch {
            while (isActive) {
                if (Debug.isDebuggerConnected() || Debug.waitingForDebugger()) {
                    android.os.Process.killProcess(android.os.Process.myPid())
                    System.exit(1)
                }
                delay(2000)
            }
        }
    }

    /**
     * Stops the periodic monitoring for debuggers by canceling the active coroutine job.
     */
    fun stopMonitoring() {
        job?.cancel()
    }
}
