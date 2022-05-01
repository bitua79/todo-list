package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.core.DateUtil
import com.example.todolist.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var listAdapter: TaskListAdapter
    private lateinit var recyclerView: RecyclerView
    private val listViewModel: TaskViewModel by activityViewModels()

    @Inject
    lateinit var dateUtil: DateUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        setupRecyclerview()
        observeData()
        setupEditTextListener()
    }

    private fun setupEditTextListener() {
        binding.etSearchField.doAfterTextChanged {
            val string = binding.etSearchField.text.toString().trim()
            if (string.isNotEmpty()) {
                listViewModel.search(binding.etSearchField.text.toString())
            } else {
                listAdapter.submitList(emptyList())
            }
        }
    }

    private fun setupRecyclerview() {
        listAdapter = TaskListAdapter(
            dateUtil
        )
        recyclerView = binding.rvCryptocurrency
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = listAdapter
    }

    private fun observeData() {
        listViewModel.tasksByQuery.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
    }
}