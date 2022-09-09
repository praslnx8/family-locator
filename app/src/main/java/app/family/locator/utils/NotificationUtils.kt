package app.family.locator.utils;

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import app.family.locator.MainActivity
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
            .setContentText(context.getString(R.string.fg_notif_desc))
            .setContentIntent(
                TaskStackBuilder.create(context).run {
                    addNextIntentWithParentStack(
                        Intent(
                            Intent.ACTION_VIEW,
                            "expense://home".toUri(),
                            context,
                            MainActivity::class.java
                        )
                    )
                    getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT)
                }
            )
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