package com.example.todolist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.todolist.core.base.BaseViewModel
import com.example.todolist.data.model.Task
import com.example.todolist.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTasks: GetTasks,
    private val addTask: AddTask,
    private val editTask: EditTask,
    private val removeTask: RemoveTask,
    private val getTasksByQuery: GetTasksByQuery
) : BaseViewModel() {
    private val query: MutableLiveData<String> = MutableLiveData()

    val allTasks = liveData { emitSource(getTasks()) }

    val tasksByQuery: LiveData<List<Task>> = query.switchMap {
        liveData(Dispatchers.IO) {
            emitSource(getTasksByQuery(query.value ?: ""))
        }
    }

    fun search(entry: String) {
        query.value = entry
    }

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