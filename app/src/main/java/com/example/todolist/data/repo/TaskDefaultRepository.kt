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
    override fun getAllTasks() = taskDao.getAllTasks()

    override fun getTasksByQuery(query: String): LiveData<List<Task>> {
        return taskDao.getTasksBQuery(query)
    }

    override fun deleteTask(task: Task) = taskDao.delete(task)

    override fun addTask(task: Task) = taskDao.add(task)

    override fun editTask(task: Task) = taskDao.edit(task.id, task.name, task.deadLine)
}