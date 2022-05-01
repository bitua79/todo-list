package com.example.todolist.ui

import androidx.navigation.fragment.findNavController
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyTaskListFragment : TaskListFragmentDelegate(priority = Priority.Low, false) {
    override fun addTask() {
        val navigation = DailyTaskListFragmentDirections.actionDailyTaskListFragmentToTaskFragment(null)
        findNavController().navigate(navigation)
    }

    override fun editTask(item: Task) {
        val navigation = DailyTaskListFragmentDirections.actionDailyTaskListFragmentToTaskFragment(item)
        findNavController().navigate(navigation)
    }
}