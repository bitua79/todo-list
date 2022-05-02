package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.core.DateUtil
import com.example.todolist.core.settings.BaseFragment
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import com.example.todolist.data.model.TaskType
import com.example.todolist.databinding.FragmentTaskListBinding
import com.example.todolist.util.getListByType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
        activity.binding.fabHome.setOnClickListener {
            addTask()
        }
        activity.binding.bottomNavigationView.menu.getItem(2).isChecked = true
    }

    private fun observeData() {
        viewModel.allTasks.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.getListByType(type, priority))
        }
    }
}