package ir.amirhesambandegan.easify_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

/**
 * A simplified utility object to show standard notifications on Android.
 */
object NotificationHelper {

    /**
     * Shows a simple text notification, automatically handling channel creation on API 26+.
     *
     * @param context The application or component [Context].
     * @param channelId The unique ID for the notification channel.
     * @param channelName The user-visible name of the notification channel.
     * @param title The title text of the notification.
     * @param message The body text of the notification.
     * @param smallIcon The drawable resource ID for the small icon.
     * @param notificationId The unique ID for this notification instance. Defaults to 1001.
     */
    fun showNotification(
        context: Context,
        channelId: String,
        channelName: String,
        title: String,
        message: String,
        smallIcon: Int,
        notificationId: Int = 1001
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }
}
