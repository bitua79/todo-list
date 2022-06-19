package com.example.todolist.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val extras = intent?.extras

        val id = extras?.getInt("NOTIFICATION_UUID")
        val name = extras?.getString("NOTIFICATION_NAME")
        val subject = extras?.getString("NOTIFICATION_SUBJECT")

        val notificationHelper =
            NotificationHelper(context)
        val nb = notificationHelper.getNotificationChannel(name ?: "task", subject ?: "subject")

        notificationHelper.manager?.notify(id?:0, nb.build())
        Log.i("Set_Task_Alarm", "Alarm for $name is set.")
    }
}