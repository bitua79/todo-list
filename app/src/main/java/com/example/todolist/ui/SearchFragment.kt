package com.example.todolist.ui

import androidx.core.widget.doAfterTextChanged
import com.example.todolist.R
import com.example.todolist.core.base.BaseTaskListFragment
import com.example.todolist.data.model.TaskType
import com.example.todolist.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment :
    BaseTaskListFragment<FragmentSearchBinding>(R.layout.fragment_search, TaskType.All) {

    override fun onInitDataBinding() {
        setupRecyclerview()
        setupEditTextListener()
        observeData()
    }

    override fun initRecyclerView() {
        recyclerView = viewBinding.rvTasks
    }

    private fun setupEditTextListener() {
        viewBinding.etSearchField.doAfterTextChanged {
            val string = viewBinding.etSearchField.text.toString().trim()
            if (string.isNotEmpty()) {
                viewModel.search(viewBinding.etSearchField.text.toString())
            } else {
                listAdapter.submitList(emptyList())
            }
        }
    }

    private fun observeData() {
        viewModel.tasksByQuery.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
    }
}