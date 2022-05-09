package com.example.todolist.core

import com.example.todolist.R
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import com.example.todolist.data.model.TaskType
import com.example.todolist.databinding.FragmentTaskListBinding
import com.example.todolist.ui.MainActivity
import com.example.todolist.util.getListByType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
abstract class BaseTaskListFragment(
    private val priority: Priority?,
    private val type: TaskType
) : BaseFragment<FragmentTaskListBinding>(R.layout.fragment_task_list, type) {

    override fun onInitDataBinding() {
        setupRecyclerview()
        setFabAction()
        observeData()
    }

    override fun initRecyclerView() {
        recyclerView = viewBinding.rvTasks
    }

    private fun setFabAction() {
        val activity = requireActivity() as MainActivity
        activity.binding.fabAdd.setOnClickListener {
            addTask()
        }
    }

    private fun observeData() {
        viewModel.allTasks.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.getListByType(type, priority))
        }
    }
}