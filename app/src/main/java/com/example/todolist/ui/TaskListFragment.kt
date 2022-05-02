package com.example.todolist.ui

import com.example.todolist.data.model.TaskType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : TaskListFragmentDelegate(null, TaskType.All)