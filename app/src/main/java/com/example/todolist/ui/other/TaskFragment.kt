package com.example.todolist.ui.other

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
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import com.example.todolist.data.model.TaskType
import com.example.todolist.databinding.FragmentTaskBinding
import com.example.todolist.util.*
import dagger.hilt.android.AndroidEntryPoint
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject


@AndroidEntryPoint
class TaskFragment : Fragment() {

    // region variables
    private lateinit var binding: FragmentTaskBinding
    private val listViewModel: TaskViewModel by activityViewModels()

    private lateinit var priorityArrayAdapter: ArrayAdapter<String>
    private lateinit var datePicker: PersianDatePickerDialog
    private lateinit var timePicker: TimePickerDialog

    private val args by navArgs<TaskFragmentArgs>()
    private var isEditPage = false

    private lateinit var deadline: PersianDate
    private lateinit var today: PersianDate

    lateinit var priority: Priority

    @Inject
    lateinit var dateUtil: DateUtils

    //endregion

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

        // Initialize persian date picker
        datePicker = initializePersianDatePicker(args.task?.deadLine, dateUtil.getThisYear())

        // initialize priority drop down
        setPriorityDropDown()

        with(binding) {
            today = dateUtil.getTodayDate()
            deadline = today

            args.task?.let {
                val time = getDate(it.deadLine)
                initValues(time)
            }

            // Set date click listener on til and tv
            tvDatePicker.setOnClickListener {
                setDatePickerClickListener()
            }
            tilDatePicker.setOnClickListener {
                setDatePickerClickListener()
            }

            // Set time click listener on til and tv
            tvTimePicker.setOnClickListener {
                setTimePickerClickListener()
            }
            tilTimePicker.setOnClickListener {
                setTimePickerClickListener()
            }

            // Invoke edit or addition
            btn.setOnClickListener {
                if (checkValidation()) {
                    if (this@TaskFragment.isEditPage) {
                        args.task?.let { task ->
                            val newTask = getTaskFromInput()
                            listViewModel.editTaskFromList(newTask)
                            val taskId = listViewModel.getTaskId(task)
                            cancelAlarm(taskId, requireContext())

                            val newTaskId = listViewModel.getTaskId(newTask)
                            startAlarm(newTaskId, requireContext(), newTask)
                        }
                    } else {
                        val newTask = getTaskFromInput()
                        listViewModel.addTaskToList(newTask)
                        val taskId = listViewModel.getTaskId(newTask)
                        startAlarm(taskId, requireContext(), newTask)
                    }

                    findNavController().navigateUp()
                }
            }
        }
    }

    // Initial fields with task (if it is edit page)
    private fun initValues(time: PersianDate) {
        with(time) {
            deadline.shYear = shYear
            deadline.shMonth = shMonth
            deadline.shDay = shDay
            deadline.hour = hour
            deadline.minute = minute
            setTimeText(hour, minute)
            setDateText(shYear, shMonth, shDay)
        }

        val item = when (args.type) {
            TaskType.Essential -> {
                Priority.High.title
            }
            TaskType.Important -> {
                Priority.Medium.title
            }
            TaskType.Daily -> {
                Priority.Low.title
            }
            else -> null
        }
        setPriorityText(item)
    }

    //region click listeners

    // Setup priority drop down
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
                    binding.tilPriority.isErrorEnabled = false
                    binding.tilPriority.error = null
                    val name = adapterView.getItemAtPosition(position).toString()
                    priority = Priority.enumValueOfTitle(name, requireContext())
                }
            }
        }
    }

    // Set date picker listener
    private fun setDatePickerClickListener() {
        with(datePicker) {
            setListener(object : PersianPickerListener {
                override fun onDateSelected(persianPickerDate: PersianPickerDate?) {
                    persianPickerDate?.let {

                        val label =
                            getPersianDateString(it.persianYear, it.persianMonth, it.persianDay)
                        // removeError
                        with(binding) {
                            tilDatePicker.isErrorEnabled = false
                            tilDatePicker.error = null
                            tvDatePicker.setText(label, false)

                            // Set variables
                            deadline.shYear = it.persianYear
                            deadline.shMonth = it.persianMonth
                            deadline.shDay = it.persianDay
                        }

                    }
                }

                override fun onDismissed() {}
            })

            args.task?.let { setInitDate(it.deadLine) }
            show()
        }
    }

    // Set time picker listener
    private fun setTimePickerClickListener() {
        // Create a timeClickHandler
        val timePickerClickHandler: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { _, h, m ->
                deadline.hour = h
                deadline.minute = m
                setTimeText(h, m)
            }

        // You can set deadline for at least one minute later
        timePicker = TimePickerDialog(
            requireContext(),
            timePickerClickHandler,
            deadline.hour,
            deadline.minute + 1,
            true
        )
        timePicker.show()
    }

    //endregion

    // region setting text
    private fun setTimeText(h: Int, m: Int) {
        val formattedTime = "${h.twoDigit()}:${m.twoDigit()}"
        binding.tvTimePicker.setText(formattedTime)
    }

    private fun setDateText(y: Int, m: Int, d: Int) {
        val formattedDate = getPersianDateString(y, m, d)
        binding.tvDatePicker.setText(formattedDate)
    }

    private fun setPriorityText(item: Int?) {
        item?.let { binding.tvPriority.setText(item) }
    }
    //endregion

    private fun getTaskFromInput(): Task {
        with(binding) {
            return Task(
                id = args.task?.id ?: 0,
                name = etName.text.toString(),
                subject = etSubject.text.toString(),
                deadLine = getPersianDate(
                    deadline.shYear,
                    deadline.shMonth,
                    deadline.shDay,
                    deadline.hour,
                    deadline.minute,
                    dateUtil.getTodayDate().second
                ),
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