package com.example.todolist.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.todolist.core.BaseViewModel
import com.example.todolist.data.model.Task
import com.example.todolist.domain.AddTask
import com.example.todolist.domain.RemoveTask
import com.example.todolist.domain.EditTask
import com.example.todolist.domain.GetTasks
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTasks: GetTasks,
    private val addTask: AddTask,
    private val editTask: EditTask,
    private val removeTask: RemoveTask,
) : BaseViewModel() {

    val allTasks = liveData { emitSource(getTasks()) }

    fun addTaskToList(t: Task) {
        addTask(t)
    }

    fun editTaskFromList(t: Task) {
        editTask(t)
    }

    fun removeTaskFromList(t: Task) {
        removeTask(t)
    }
}