package com.example.todolist.data.repo

import androidx.lifecycle.LiveData
import com.example.todolist.data.local.TaskDao
import com.example.todolist.data.model.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskDefaultRepository @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override fun getTaskId(task: Task) = taskDao.getTaskId(task.name, task.subject)

    override fun getAllTasks() = taskDao.getAllTasks()

    override fun getTasksByQuery(query: String) = taskDao.getTasksBQuery(query)

    override fun deleteTask(task: Task) = taskDao.deleteTask(task)

    override fun addTask(task: Task) = taskDao.addTask(task)

    override fun editTask(task: Task) = taskDao.editTask(task.id, task.name, task.deadLine)
}