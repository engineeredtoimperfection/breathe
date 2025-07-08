package com.github.engineeredtoimperfection.breathe.common

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity.NOTIFICATION_SERVICE
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.github.engineeredtoimperfection.breathe.MainActivity
import com.github.engineeredtoimperfection.breathe.R

data class NotificationChannelSettings(
    val channelID: String,
    val channelName: String,
    val description: String,
    val importance: Int
) {

    fun toNotificationChannel(): NotificationChannel {
        return NotificationChannel(channelID, channelName, importance).apply {
            description = description
        }
    }
}

val remindersNotificationChannelSettings = NotificationChannelSettings(
    channelID = "Reminders",
    channelName = "Gentle Reminders",
    description = "Gentle nudges to keep you calm and relaxed.",
    importance = NotificationManager.IMPORTANCE_DEFAULT
)

fun Activity.notificationManager(): NotificationManager {
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager
}

fun createRemindersNotificationChannel(activity: Activity) {
    createNotificationChannel(activity, remindersNotificationChannelSettings)
}

fun createNotificationChannel(
    activity: Activity,
    notificationChannelSettings: NotificationChannelSettings
) {

    val channel = notificationChannelSettings.toNotificationChannel()

    // Register the channel with the system. You can't change the importance
    // or other notification behaviors after this.
    activity.notificationManager().createNotificationChannel(channel)
}

fun sendReminderNotification(activity: Activity) {
    val remindersChannelId = remindersNotificationChannelSettings.channelID

    val reminderNotification = activity.createNotification(channelId = remindersChannelId)
    activity.sendNotification(notification = reminderNotification, notificationId = 0)
}

fun Activity.createNotification(
    channelId: String,
    smallIcon: Int = R.drawable.ic_launcher_new_foreground,
    contentTitle: String = "Gentle Reminder",
    contentText: String = "Have you taken a moment to breathe today?"
): Notification {
    val intent = Intent(this, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    val pendingIntent: PendingIntent =
        PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val notification = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(smallIcon)
        .setContentTitle(contentTitle)
        .setContentText(contentText)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    return notification.build()
}

fun Activity.sendNotification(notification: Notification, notificationId: Int) {

    val isPermissionGranted = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

    if (!isPermissionGranted) {
        requestPermissionFromUser()
    } else {
        notificationManager().notify(notificationId, notification)
    }

}

fun Activity.requestPermissionFromUser() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
        0
    )
}
