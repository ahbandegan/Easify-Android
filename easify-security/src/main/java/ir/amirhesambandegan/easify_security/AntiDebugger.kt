package ir.amirhesambandegan.easify_security

import android.os.Debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

/**
 * Periodically detects attached debuggers and executes the configured response action.
 */
object AntiDebugger {

    private val lock = Any()
    private val defaultScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Volatile
    private var job: Job? = null

    /**
     * Starts periodic debugger monitoring on [scope]. Calling again while already active is a no-op.
     *
     * @param scope Coroutine scope used for the polling loop.
     * @param intervalMillis Poll interval in milliseconds. Defaults to `2000L`.
     * @param onDebuggerDetected Invoked when a debugger is attached; defaults to terminating the process.
     */
    fun startMonitoring(
        scope: CoroutineScope = defaultScope,
        intervalMillis: Long = 2_000L,
        onDebuggerDetected: () -> Unit = {
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        }
    ) {
        synchronized(lock) {
            if (job?.isActive == true) return
            job = scope.launch {
                while (isActive) {
                    if (Debug.isDebuggerConnected() || Debug.waitingForDebugger()) {
                        onDebuggerDetected()
                        break
                    }
                    delay(intervalMillis.coerceAtLeast(250L))
                }
            }
        }
    }

    /**
     * Stops periodic debugger monitoring and clears the tracked coroutine handle.
     */
    fun stopMonitoring() {
        synchronized(lock) {
            job?.cancel()
            job = null
        }
    }
}
