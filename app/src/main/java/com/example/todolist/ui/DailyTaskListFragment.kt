package com.example.todolist.ui

import com.example.todolist.core.base.CommonTaskListFragment
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.TaskType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyTaskListFragment : CommonTaskListFragment(priority = Priority.Low, type = TaskType.Daily)
