package com.example.todolist.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.todolist.core.BaseViewModel
import com.example.todolist.domain.GetTasks
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTasks: GetTasks,
) : BaseViewModel() {
    private val query: MutableLiveData<String> = MutableLiveData()

    val allTasks = liveData { emitSource(getTasks()) }
}