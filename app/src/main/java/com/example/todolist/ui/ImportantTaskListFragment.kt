package com.example.todolist.ui

import androidx.navigation.fragment.findNavController
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ImportantTaskListFragment : TaskListFragmentDelegate(priority = Priority.Medium, false) {
    override fun addTask() {
        val navigation =
            ImportantTaskListFragmentDirections.actionImportantTaskListFragmentToTaskFragment(null)
        findNavController().navigate(navigation)
    }

    override fun editTask(item: Task) {
        val navigation =
            ImportantTaskListFragmentDirections.actionImportantTaskListFragmentToTaskFragment(item)
        findNavController().navigate(navigation)
    }
}