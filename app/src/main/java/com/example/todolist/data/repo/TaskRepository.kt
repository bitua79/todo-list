package com.example.todolist.data.repo

import androidx.lifecycle.LiveData
import com.example.todolist.data.model.Task

interface TaskRepository {
    fun getAllTasks(): LiveData<List<Task>>

    fun getTasksByQuery(query: String): LiveData<List<Task>>

    fun deleteTask(task: Task)

    fun addTask(task: Task)

    fun editTask(task: Task)
}