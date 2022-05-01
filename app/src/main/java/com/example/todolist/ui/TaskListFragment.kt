package com.example.todolist.ui

import androidx.navigation.fragment.findNavController
import com.example.todolist.data.model.Task
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : TaskListFragmentDelegate(null, true) {

    override fun addTask() {
        val navigation = TaskListFragmentDirections.actionTaskListFragmentToTaskFragment(null)
        findNavController().navigate(navigation)
    }

    override fun editTask(item: Task) {
        val navigation = TaskListFragmentDirections.actionTaskListFragmentToTaskFragment(item)
        findNavController().navigate(navigation)
    }
}