package com.example.todolist.ui

import androidx.navigation.fragment.findNavController
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EssentialTaskListFragment : TaskListFragmentDelegate(priority = Priority.High, false) {
    override fun addTask() {
        val navigation =
            EssentialTaskListFragmentDirections.actionEssentialTaskListFragmentToTaskFragment(null)
        findNavController().navigate(navigation)
    }

    override fun editTask(item: Task) {
        val navigation =
            EssentialTaskListFragmentDirections.actionEssentialTaskListFragmentToTaskFragment(item)
        findNavController().navigate(navigation)
    }
}