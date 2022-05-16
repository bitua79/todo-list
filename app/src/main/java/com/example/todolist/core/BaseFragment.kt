package com.example.todolist.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.model.Task
import com.example.todolist.data.model.TaskType
import com.example.todolist.ui.TaskFragmentDirections
import com.example.todolist.ui.TaskListAdapter
import com.example.todolist.ui.TaskListFragmentDirections
import com.example.todolist.ui.TaskViewModel
import javax.inject.Inject

abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutId: Int,
    private val taskType: TaskType
) : Fragment() {
    protected lateinit var viewBinding: B

    lateinit var navController: NavController

    @Inject
    lateinit var dateUtil: DateUtil

    lateinit var listAdapter: TaskListAdapter
    lateinit var recyclerView: RecyclerView
    val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        onInitDataBinding()
        setupRecyclerview()
    }

    abstract fun onInitDataBinding()

    abstract fun initRecyclerView()

    fun setupRecyclerview() {
        listAdapter = TaskListAdapter(
            dateUtil,
            { task -> doneTask(task) },
            { task -> editTask(task) }
        )

        initRecyclerView()
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = listAdapter
    }

    fun addTask() {
        navController.navigate(TaskListFragmentDirections.actionToTaskFragment(null, taskType))
    }

    private fun editTask(item: Task) {
        navController.navigate(
            TaskListFragmentDirections.actionToTaskFragment(
                item,
                TaskType.All
            )
        )
    }

    private fun doneTask(item: Task) {
        val newItem = Task(
            name = item.name,
            subject = item.subject,
            deadLine = item.deadLine,
            remainTime = item.remainTime,
            priority = item.priority,
            isDone = true
        )
        viewModel.removeTaskFromList(item)
        viewModel.addTaskToList(newItem)
    }
}
