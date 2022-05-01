package com.example.todolist.ui

import androidx.navigation.fragment.findNavController
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoneTaskListFragment : TaskListFragmentDelegate(priority = Priority.Done, false){
    override fun addTask() {
        val navigation = DoneTaskListFragmentDirections.actionDoneTaskListFragmentToTaskFragment(null)
        findNavController().navigate(navigation)
    }

    override fun editTask(item: Task) {
        val navigation = DoneTaskListFragmentDirections.actionDoneTaskListFragmentToTaskFragment(item)
        findNavController().navigate(navigation)
    }
}