package com.example.todolist.domain

import com.example.todolist.data.model.Task
import com.example.todolist.data.repo.TaskRepository
import javax.inject.Inject

class AddTask @Inject constructor(
    private val repo: TaskRepository
) {
    operator fun invoke(task: Task){
        repo.addTask(task)
    }
}