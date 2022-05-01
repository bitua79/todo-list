package com.example.todolist.domain

import com.example.todolist.data.model.Task
import com.example.todolist.data.repo.TaskRepository
import javax.inject.Inject

class RemoveTask @Inject constructor(
    private val repo: TaskRepository
) {
    operator fun invoke(task: Task) {
        return repo.deleteTask(task)
    }
}