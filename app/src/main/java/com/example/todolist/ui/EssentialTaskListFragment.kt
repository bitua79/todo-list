package com.example.todolist.ui

import android.os.Bundle
import android.view.View
import com.example.todolist.data.model.Priority
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EssentialTaskListFragment : TaskListFragmentDelegate(priority = Priority.High, false)