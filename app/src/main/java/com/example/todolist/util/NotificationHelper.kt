package com.example.todolist.util

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.todolist.R

class NotificationHelper(base: Context?) : ContextWrapper(base) {
    private var mManager: NotificationManager? = null

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_HIGH)
        manager?.createNotificationChannel(channel)
    }

    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }


    fun getNotificationChannel(taskName: String, taskSubject: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, ID)
            .setContentTitle(getString(R.string.header_notification, taskName))
            .setContentText(taskSubject)
            .setSmallIcon(R.mipmap.ic_launcher)

    }

    companion object{
        const val ID = "Channel_ID"
        const val NAME = "Channel_Name"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }
}