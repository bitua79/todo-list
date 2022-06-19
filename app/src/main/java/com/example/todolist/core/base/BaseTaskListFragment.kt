package com.example.todolist.core.base

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
import com.example.todolist.ui.list.TaskListAdapter
import com.example.todolist.ui.list.TaskListFragmentDirections
import com.example.todolist.ui.other.TaskViewModel
import com.example.todolist.util.DateUtils
import javax.inject.Inject

abstract class BaseTaskListFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutId: Int,
    private val taskType: TaskType
) : Fragment() {
    protected lateinit var viewBinding: B

    lateinit var navController: NavController

    @Inject
    lateinit var dateUtil: DateUtils

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

    //region initialize an navigation
    abstract fun onInitDataBinding()

    abstract fun initRecyclerView()

    fun setupRecyclerview() {
        listAdapter = TaskListAdapter(
            dateUtil,
            onItemClicked = { task -> goToEditTaskPage(task) }
        )

        initRecyclerView()
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = listAdapter
    }

    fun goToAddTaskPage() {
        navController.navigate(TaskListFragmentDirections.actionToTaskFragment(null, taskType))
    }

    private fun goToEditTaskPage(item: Task) {
        navController.navigate(
            TaskListFragmentDirections.actionToTaskFragment(
                item,
                TaskType.All
            )
        )
    }
    //endregion

    // region task actions
    fun addTask(item: Task) {
        viewModel.addTaskToList(item)
    }

    fun doneTask(item: Task, isDone: Boolean) {
        val newItem = Task(
            id = item.id,
            name = item.name,
            subject = item.subject,
            deadLine = item.deadLine,
            remainTime = item.remainTime,
            priority = item.priority,
            isDone = isDone
        )
        viewModel.removeTaskFromList(item)
        viewModel.addTaskToList(newItem)
    }

    fun removeTask(item: Task) {
        viewModel.removeTaskFromList(item)
    }
    // endregion
}
