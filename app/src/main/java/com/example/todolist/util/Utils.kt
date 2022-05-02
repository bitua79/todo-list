package com.example.todolist.util

import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import com.example.todolist.data.model.TaskType

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
    return when (type) {
        TaskType.All -> this.sortList()
        TaskType.Done -> this.filter { it.isDone }
        else -> this.filter { it.priority == priority && !it.isDone }
    }

}
