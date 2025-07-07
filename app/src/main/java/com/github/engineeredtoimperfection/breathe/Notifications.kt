package com.github.engineeredtoimperfection.breathe

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity.NOTIFICATION_SERVICE
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

const val REMINDERS_CHANNEL_ID = "Reminders"

fun Activity.notificationManager(): NotificationManager {
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager
}

fun createRemindersNotificationChannel(activity: Activity) {
    createNotificationChannel(activity, REMINDERS_CHANNEL_ID)
}

fun createNotificationChannel(activity: Activity, channelID: String) {
    // Create the NotificationChannel.
    val name = "Gentle Reminders"
    val descriptionText = "Gentle nudges to keep you calm and relaxed."
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val mChannel = NotificationChannel(channelID, name, importance)
    mChannel.description = descriptionText
    // Register the channel with the system. You can't change the importance
    // or other notification behaviors after this.
    val notificationManager = activity.notificationManager()
    notificationManager.createNotificationChannel(mChannel)
}

fun sendReminderNotification(activity: Activity) {
    activity.sendNotification(REMINDERS_CHANNEL_ID)
}

fun Activity.sendNotification(channelID: String) {
    val notificationManager = notificationManager()

    val intent = Intent(this, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    val pendingIntent: PendingIntent =
        PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val notification = NotificationCompat.Builder(this, channelID)
        .setSmallIcon(R.drawable.ic_launcher_new_foreground)
        .setContentTitle("Gentle Reminder")
        .setContentText("Have you taken a moment to breathe today?")
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            0
        )

        return
    }

    notificationManager.notify(0, notification.build())

}