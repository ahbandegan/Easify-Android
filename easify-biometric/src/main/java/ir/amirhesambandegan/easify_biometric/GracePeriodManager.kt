package ir.amirhesambandegan.easify_biometric

/**
 * Manages the biometric authentication grace period. During the grace period,
 * the user is not required to re-authenticate when the app resumes.
 */
object GracePeriodManager {
    /**
     * The timestamp of the last successful unlock, in milliseconds.
     */
    private var lastUnlockTime: Long = 0

    /**
     * The duration of the grace period in milliseconds. Defaults to 30,000ms (30 seconds).
     */
    private var gracePeriodMillis: Long = 30000 // default 30 seconds

    /**
     * Updates the last unlock time to the current system time.
     * Should be called when authentication is successfully completed.
     */
    fun updateUnlockTime() {
        lastUnlockTime = System.currentTimeMillis()
    }

    /**
     * Checks if the grace period is still active based on the last unlock time.
     * 
     * @return True if the grace period is active, false otherwise.
     */
    fun isGracePeriodActive(): Boolean {
        return (System.currentTimeMillis() - lastUnlockTime) < gracePeriodMillis
    }

    /**
     * Sets a custom grace period duration.
     * 
     * @param seconds The new grace period duration in seconds.
     */
    fun setGracePeriod(seconds: Long) {
        gracePeriodMillis = seconds * 1000
    }
}
