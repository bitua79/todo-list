package com.example.todolist.data.repo

import androidx.lifecycle.LiveData
import com.example.todolist.data.local.TaskDao
import com.example.todolist.data.model.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getAllTasks() = taskDao.getAllTasks()

    fun deleteTask(task: Task) = taskDao.delete(task)

    fun addTask(task: Task) = taskDao.add(task)

    fun editTask(task: Task) = taskDao.edit(task.name, task.deadLine)}