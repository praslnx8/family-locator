package app.family.locator.utils;

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.family.locator.R

class NotificationUtils(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat
) {
    fun getForegroundNotification(): Notification {
        createNotificationChannel(
            FOREGROUND_SERVICE_CHANNEL_ID,
            context.getString(R.string.fg_notif_channel_name),
            context.getString(R.string.fg_notif_channel_desc)

        )

        return buildForegroundNotification()

    }

    private fun buildForegroundNotification(): Notification {
        return NotificationCompat.Builder(context, FOREGROUND_SERVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setContentTitle(context.getString(R.string.fg_notif_title))
            .setOngoing(true)
            .setCategory(Notification.CATEGORY_LOCATION_SHARING)
            .setContentText(context.getString(R.string.fg_notif_desc))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel(id: String, name: String, description: String) {
        notificationManager.createNotificationChannel(
            NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW).also {
                it.description = description
            }
        )
    }

    companion object {
        private const val FOREGROUND_SERVICE_CHANNEL_ID = "foreground_service"
    }
}