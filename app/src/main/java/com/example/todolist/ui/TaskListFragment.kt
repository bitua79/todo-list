package com.example.todolist.ui

import com.example.todolist.core.base.CommonTaskListFragment
import com.example.todolist.data.model.TaskType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : CommonTaskListFragment(null, TaskType.All)