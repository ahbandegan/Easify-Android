package ir.amirhesambandegan.easify_biometric

object GracePeriodManager {
    private var lastUnlockTime: Long = 0
    private var gracePeriodMillis: Long = 30000 // default 30 seconds

    fun updateUnlockTime() {
        lastUnlockTime = System.currentTimeMillis()
    }

    fun isGracePeriodActive(): Boolean {
        return (System.currentTimeMillis() - lastUnlockTime) < gracePeriodMillis
    }

    fun setGracePeriod(seconds: Long) {
        gracePeriodMillis = seconds * 1000
    }
}
