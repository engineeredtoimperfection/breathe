package com.github.engineeredtoimperfection.breathe.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.github.engineeredtoimperfection.breathe.ACTION_GENTLE_NUDGE") {
            context?.let {
                sendReminderNotification(context)
            }
        }
    }
}