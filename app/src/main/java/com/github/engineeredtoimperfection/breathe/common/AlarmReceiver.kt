package com.github.engineeredtoimperfection.breathe.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "Received alarm")

        if (intent?.action == INTENT_ACTION_GENTLE_NUDGE) {

            Log.d("AlarmReceiver", "Received gentle nudge")

            context?.let {

                Log.d("AlarmReceiver", "Sending reminder notification")

                sendReminderNotification(context)
            }
        }
    }
}