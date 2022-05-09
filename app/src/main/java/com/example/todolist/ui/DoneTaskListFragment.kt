package com.example.todolist.ui

import com.example.todolist.core.BaseTaskListFragment
import com.example.todolist.data.model.TaskType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoneTaskListFragment : BaseTaskListFragment(priority = null, type = TaskType.Done)