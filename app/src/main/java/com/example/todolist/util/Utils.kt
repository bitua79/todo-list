package com.example.todolist.util

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
