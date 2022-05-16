package com.example.todolist.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.R
import com.example.todolist.core.DateUtil
import com.example.todolist.core.getDate
import com.example.todolist.core.getPersianDate
import com.example.todolist.core.twoDigit
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import com.example.todolist.data.model.TaskType
import com.example.todolist.databinding.FragmentTaskBinding
import com.example.todolist.util.*
import dagger.hilt.android.AndroidEntryPoint
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import javax.inject.Inject
import kotlin.math.min


@AndroidEntryPoint
class TaskFragment : Fragment() {
    private lateinit var binding: FragmentTaskBinding
    private val listViewModel: TaskViewModel by activityViewModels()

    private lateinit var priorityArrayAdapter: ArrayAdapter<String>
    private lateinit var datePicker: PersianDatePickerDialog
    private lateinit var timePicker: TimePickerDialog

    private val args by navArgs<TaskFragmentArgs>()
    private var isEditPage = false

    private var minute: Int = 0
    private var hour: Int = 0
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    lateinit var priority: Priority

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isEditPage = args.task != null
        binding.isEdit = isEditPage
        binding.task = args.task
        initViews()
    }

    private fun initViews() {

        datePicker = initializePersianDatePicker()
        setPriorityDropDown()

        with(binding) {

            tvDatePicker.setOnClickListener {
                setDatePickerClickListener()
            }
            tilDatePicker.setOnClickListener {
                setDatePickerClickListener()
            }

            tvTimePicker.setOnClickListener {
                setTimePickerClickListener()
            }
            tilTimePicker.setOnClickListener {
                setTimePickerClickListener()
            }

            btn.setOnClickListener {
                if (checkValidation()) {
                    if (this@TaskFragment.isEditPage)
                        listViewModel.editTaskFromList(getTaskFromInput())
                    else
                        listViewModel.addTaskToList(getTaskFromInput())

                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun setPriorityDropDown() {
        if (!isEditPage) {
            priorityArrayAdapter = ArrayAdapter(
                requireContext(),
                R.layout.item_list_popup_window,
                Priority.values().toList().map { requireContext().getString(it.title) }
            )

            with(binding.tvPriority) {
                setAdapter(priorityArrayAdapter)

                setOnItemClickListener { adapterView, _, position, _ ->
                    val name = adapterView.getItemAtPosition(position).toString()
                    priority = Priority.enumValueOfTitle(name, requireContext())
                }

                when (args.type) {
                    TaskType.Essential -> {
                        setText(Priority.High.title)

                    }
                    TaskType.Important -> {
                        setText(Priority.Medium.title)
                    }

                    TaskType.Daily -> {
                        setText(Priority.Low.title)
                    }
                    else -> {
                        // Default string
                    }
                }

            }
        }
    }

    private fun setDatePickerClickListener() {
        datePicker.setDatePickerClickHandler(
            completeTextView = binding.tvDatePicker,
            action = {
                year = it?.persianYear ?: 0
                month = it?.persianMonth ?: 0
                day = it?.persianDay ?: 0
            },
            removeError = {
                with(binding) {
                    tilDatePicker.isErrorEnabled = false
                    tilDatePicker.error = null
                }
            })
    }

    private fun setTimePickerClickListener() {
        timePicker = TimePickerDialog(
            requireContext(),
            timePickerClickHandler,
            23,
            59,
            true
        )
        timePicker.show()
    }

    private val timePickerClickHandler: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, h, m ->
            val formattedTime: String = when {
                h == 0 -> {
                    if (m < 10) {
                        "${h.twoDigit()}:0${m.twoDigit()}"
                    } else {
                        "${h.twoDigit()}:${m.twoDigit()}"
                    }
                }
                h > 12 -> {
                    if (m < 10) {
                        "${h.twoDigit()}:0${m.twoDigit()}"
                    } else {
                        "${h.twoDigit()}:${m.twoDigit()}"
                    }
                }
                h == 12 -> {
                    if (m < 10) {
                        "${h.twoDigit()}:${m.twoDigit()}"
                    } else {
                        "${h.twoDigit()}:${m.twoDigit()}"
                    }
                }
                else -> {
                    if (m < 10) {
                        "${h.twoDigit()}:${m.twoDigit()}"
                    } else {
                        "${h.twoDigit()}:${m.twoDigit()}"
                    }
                }
            }
            hour = h
            minute = m
            binding.tvTimePicker.setText(formattedTime)
        }

    private fun getTaskFromInput(): Task {
        with(binding) {
            return Task(
                name = etName.text.toString(),
                subject = etSubject.text.toString(),
                deadLine = getPersianDate(year, month, day, hour, minute, 0),
                remainTime = null,
                priority = Priority.enumValueOfTitle(
                    tvPriority.text.toString(),
                    requireContext()
                ),
                isDone = false
            )
        }
    }

    private fun checkValidation(): Boolean {
        var valid = true
        with(binding) {
            if (
                etName.isEmpty(tilPriority) ||
                tvDatePicker.isEmpty(tilDatePicker) ||
                tvTimePicker.isEmpty(tilTimePicker) ||
                tvPriority.isEmpty(tilPriority)
            ) {
                valid = false
            }
        }
        return valid
    }


}