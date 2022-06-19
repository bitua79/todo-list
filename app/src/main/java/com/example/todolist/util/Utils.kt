package com.example.todolist.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import com.example.todolist.data.model.TaskType
import java.util.*


fun List<Task>.sortList(): MutableList<Task> {
    val high = mutableListOf<Task>()
    val medium = mutableListOf<Task>()
    val low = mutableListOf<Task>()

    forEach {
        if (it.priority == Priority.High)
            high.add(it)
        if (it.priority == Priority.Medium)
            medium.add(it)
        if (it.priority == Priority.Low)
            low.add(it)
    }

    high.sortBy { it.deadLine }
    medium.sortBy { it.deadLine }
    low.sortBy { it.deadLine }

    val sortedList = mutableListOf<Task>()

    sortedList.addAll(high)
    sortedList.addAll(medium)
    sortedList.addAll(low)

    return sortedList
}

fun List<Task>.getListByType(type: TaskType?, priority: Priority?): List<Task> {
    val list =  when (type) {
        TaskType.All -> this.sortList().filter { !it.isDone }
        TaskType.Done -> this.filter { it.isDone }
        else -> this.filter { it.priority == priority && !it.isDone }
    }

    return list.sortedBy { it.deadLine }
}

fun getZeroInDefaultLocale(): String {
    return String.format(Locale.getDefault(), "%d", 0)
}

fun Int.twoDigit(): String {     // To handle one digit fields => 0:0 -> 00:00
    return String.format(Locale.getDefault(), "%2d", this)
        .replace(" ", getZeroInDefaultLocale())
}

fun startAlarm(uuid: Int, context: Context, task: Task) {
    val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlertReceiver::class.java)

    val bundle = Bundle()
    bundle.putInt("NOTIFICATION_UUID", uuid)
    bundle.putString("NOTIFICATION_NAME", task.name)
    bundle.putString("NOTIFICATION_SUBJECT", task.subject)
    intent.putExtras(bundle)

    val pendingIntent = PendingIntent.getBroadcast(context, uuid, intent, 0)

    val c = getCalendar(task.deadLine)
    alarmManager.set(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    Log.i("Set_Task_Alarm","Alarm for task ${task.name} is set for ${c.timeInMillis}")
}

fun cancelAlarm(uuid: Int, context: Context) {
    val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlertReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, uuid, intent, 0)
    alarmManager.cancel(pendingIntent)
}