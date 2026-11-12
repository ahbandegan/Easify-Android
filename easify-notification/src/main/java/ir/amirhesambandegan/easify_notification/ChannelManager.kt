package ir.amirhesambandegan.easify_notification

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build

/**
 * Safely creates and configures Android Notification Channels and Channel Groups (API 26+).
 */
object ChannelManager {

    fun createGroup(context: Context, groupId: String, groupName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                as? NotificationManager ?: return
            manager.createNotificationChannelGroup(NotificationChannelGroup(groupId, groupName))
        }
    }

    fun createChannel(
        context: Context,
        channelId: String,
        channelName: String,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        description: String? = null,
        groupId: String? = null,
        soundUri: Uri? = null,
        enableLights: Boolean = true,
        lightColor: Int = Color.BLUE,
        enableVibration: Boolean = true,
        vibrationPattern: LongArray = longArrayOf(0L, 250L, 250L, 250L)
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                as? NotificationManager ?: return
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                this.description = description
                this.group = groupId
                this.enableLights(enableLights)
                this.lightColor = lightColor
                this.enableVibration(enableVibration)
                this.vibrationPattern = vibrationPattern

                if (soundUri != null) {
                    val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                    setSound(soundUri, audioAttributes)
                } else if (importance < NotificationManager.IMPORTANCE_DEFAULT) {
                    setSound(null, null)
                }
            }
            manager.createNotificationChannel(channel)
        }
    }
}
