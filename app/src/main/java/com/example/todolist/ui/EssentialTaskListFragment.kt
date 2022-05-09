package com.example.todolist.ui

import com.example.todolist.core.BaseTaskListFragment
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.TaskType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EssentialTaskListFragment : BaseTaskListFragment(priority = Priority.High, type = TaskType.Essential)