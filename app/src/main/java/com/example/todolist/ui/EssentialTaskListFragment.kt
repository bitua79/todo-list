package com.example.todolist.ui

import com.example.todolist.data.model.Priority
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EssentialTaskListFragment : TaskListFragmentDelegate(priority = Priority.High, false)