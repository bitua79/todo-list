package com.example.todolist.ui.list

import com.example.todolist.core.base.CommonTaskListFragment
import com.example.todolist.data.model.TaskType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoneTaskListFragment : CommonTaskListFragment(priority = null, type = TaskType.Done)