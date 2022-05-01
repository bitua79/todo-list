package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.core.DateUtil
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import com.example.todolist.databinding.FragmentTaskListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class TaskListFragmentDelegate(
    private val priority: Priority ,
    private val isMainList: Boolean
): Fragment(){
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

        //Fake data
        listAdapter.submitList(
            listOf(
                Task(
                    name = "task1",
                    deadLine = 122456778L,
                    isDone = false,
                    priority = priority
                ),
                Task(
                    name = "task1",
                    deadLine = 122456778L,
                    isDone = false,
                    priority = priority
                ),
                Task(
                    name = "task1",
                    deadLine = 122456778L,
                    isDone = false,
                    priority = priority
                ),
                Task(
                    name = "task1",
                    deadLine = 122456778L,
                    isDone = false,
                    priority = priority
                ),
                Task(
                    name = "task1",
                    deadLine = 122456778L,
                    isDone = false,
                    priority = priority
                )
            )
        )
    }

    private fun addTask() {

    }

    private fun doneTask(item: Task) {
    }

    private fun editTask(item: Task) {

    }
}