package ir.amirhesambandegan.easify_notification

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object ScheduledNotification {

    /**
     * Schedules a local notification to appear after a specific delay.
     * Note: Requires a custom Worker class in the app module to actually show the notification.
     */
    inline fun <reified W : CoroutineWorker> schedule(
        context: Context,
        delayMillis: Long,
        inputData: Data = workDataOf()
    ): java.util.UUID {
        val request = OneTimeWorkRequestBuilder<W>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(request)
        return request.id
    }
    
    fun cancel(context: Context, workId: java.util.UUID) {
        WorkManager.getInstance(context).cancelWorkById(workId)
    }
}
