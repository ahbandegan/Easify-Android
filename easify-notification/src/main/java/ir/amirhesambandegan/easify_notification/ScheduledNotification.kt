package ir.amirhesambandegan.easify_notification

import android.content.Context
import androidx.work.*
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * Utility object for scheduling delayed background notification tasks using [WorkManager].
 */
object ScheduledNotification {

    /**
     * Schedules a local notification or background work to execute after a specified delay.
     *
     * @param W The type of [CoroutineWorker] responsible for executing the background notification task.
     * @param context The application or component [Context].
     * @param delayMillis The delay in milliseconds before the worker task is enqueued for execution.
     * @param inputData Optional [Data] key-value payload to pass to the worker. Defaults to empty data.
     * @return The unique [UUID] identifier assigned to the scheduled work request.
     */
    inline fun <reified W : CoroutineWorker> schedule(
        context: Context,
        delayMillis: Long,
        inputData: Data = workDataOf()
    ): UUID {
        val request = OneTimeWorkRequestBuilder<W>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(request)
        return request.id
    }
    
    /**
     * Cancels a previously scheduled background work request by its identifier.
     *
     * @param context The application or component [Context].
     * @param workId The unique [UUID] of the scheduled work request to cancel.
     */
    fun cancel(context: Context, workId: UUID) {
        WorkManager.getInstance(context).cancelWorkById(workId)
    }
}
