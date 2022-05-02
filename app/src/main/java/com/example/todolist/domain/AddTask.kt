package com.example.todolist.domain

import com.example.todolist.data.model.Task
import com.example.todolist.data.repo.TaskDefaultRepository
import javax.inject.Inject

class AddTask @Inject constructor(
    private val repo: TaskDefaultRepository
) {
    operator fun invoke(task: Task){
        repo.addTask(task)
    }
}