package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.core.DateUtil
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import com.example.todolist.databinding.FragmentTaskListBinding
import com.example.todolist.util.getListByPriority
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class TaskListFragmentDelegate(
    private val priority: Priority?,
    private val isMainList: Boolean
) : Fragment() {
    lateinit var binding: FragmentTaskListBinding
    private lateinit var listAdapter: TaskListAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel: TaskViewModel by activityViewModels()

    @Inject
    lateinit var dateUtil: DateUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        setupRecyclerview()

        observeData()

        with(binding) {
            fabIsVisible = isMainList
            fabAddTask.setOnClickListener {
                addTask()
            }
        }
    }

    // initialize adapter
    private fun setupRecyclerview() {
        listAdapter = TaskListAdapter(
            dateUtil,
            { task ->
                doneTask(task)
            },
            { task -> editTask(task) }
        )

        recyclerView = binding.rvTasks
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = listAdapter
    }

    private fun observeData() {
        viewModel.allTasks.observe(viewLifecycleOwner) {
            listAdapter.submitList(it.getListByPriority(priority))
        }
    }

    private fun addTask(){
        findNavController().navigate(TaskListFragmentDirections.actionToTaskFragment(null))
    }

    private fun editTask(item: Task){
        findNavController().navigate(TaskListFragmentDirections.actionToTaskFragment(item))
    }

    private fun doneTask(item: Task) {
        val newItem = Task(
            name = item.name,
            deadLine = item.deadLine,
            remainTime = item.remainTime,
            priority = Priority.Done,
            isDone = item.isDone
        )
        viewModel.removeTaskFromList(item)
        viewModel.addTaskToList(newItem)
    }
}