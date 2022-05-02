package com.example.todolist.ui

import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.TaskType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EssentialTaskListFragment : TaskListFragmentDelegate(priority = Priority.High, type = TaskType.Essential)