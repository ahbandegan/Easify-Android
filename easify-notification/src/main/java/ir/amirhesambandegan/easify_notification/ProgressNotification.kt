package ir.amirhesambandegan.easify_notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ProgressNotification(
    private val context: Context,
    private val channelId: String,
    private val notificationId: Int,
    private val title: String,
    private val iconRes: Int
) {
    private val builder = NotificationCompat.Builder(context, channelId)
        .setContentTitle(title)
        .setContentText("Starting...")
        .setSmallIcon(iconRes)
        .setPriority(NotificationCompat.PRIORITY_LOW)

    private val manager = NotificationManagerCompat.from(context)

    fun updateProgress(progress: Int, max: Int = 100, message: String = "$progress%") {
        builder.setProgress(max, progress, false)
            .setContentText(message)
        notifyManager()
    }

    fun setIndeterminate(message: String = "Processing...") {
        builder.setProgress(0, 0, true)
            .setContentText(message)
        notifyManager()
    }

    fun finish(successMessage: String = "Complete") {
        builder.setProgress(0, 0, false)
            .setContentText(successMessage)
        notifyManager()
    }
    
    fun cancel() {
        manager.cancel(notificationId)
    }

    private fun notifyManager() {
        try {
            manager.notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
