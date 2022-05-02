package com.example.todolist.domain

import androidx.lifecycle.LiveData
import com.example.todolist.data.model.Task
import com.example.todolist.data.repo.TaskDefaultRepository
import javax.inject.Inject

class GetTasksByQuery @Inject constructor(
    private val repo: TaskDefaultRepository
) {
    operator fun invoke(query: String): LiveData<List<Task>> {
        return repo.getTasksByQuery(query)
    }
}