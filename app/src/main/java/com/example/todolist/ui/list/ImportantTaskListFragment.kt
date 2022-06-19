package com.example.todolist.ui.list

import com.example.todolist.core.base.CommonTaskListFragment
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.TaskType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ImportantTaskListFragment : CommonTaskListFragment(priority = Priority.Medium, TaskType.Important)