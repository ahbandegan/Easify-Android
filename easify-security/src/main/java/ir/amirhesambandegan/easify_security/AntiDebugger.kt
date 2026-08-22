package ir.amirhesambandegan.easify_security

import android.os.Debug
import kotlinx.coroutines.*

object AntiDebugger {

    private var job: Job? = null

    /**
     * Periodically checks if a debugger is attached. If so, immediately kills the app
     * to prevent reverse engineering and memory dumping.
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

    fun stopMonitoring() {
        job?.cancel()
    }
}
