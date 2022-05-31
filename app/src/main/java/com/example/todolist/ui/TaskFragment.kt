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
import com.example.todolist.data.model.Priority
import com.example.todolist.data.model.Task
import com.example.todolist.data.model.TaskType
import com.example.todolist.databinding.FragmentTaskBinding
import com.example.todolist.util.getPersianDateString
import com.example.todolist.util.initializePersianDatePicker
import com.example.todolist.util.isEmpty
import com.example.todolist.util.twoDigit
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

    private var minute: Int = 59
    private var hour: Int = 23
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    lateinit var priority: Priority

    @Inject
    lateinit var dateUtil: DateUtil

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

            args.task?.let {
                val time = getDate(it.deadLine)
                hour = time.hour
                minute = time.minute
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
                    if (this@TaskFragment.isEditPage)
                        listViewModel.editTaskFromList(getTaskFromInput())
                    else
                        listViewModel.addTaskToList(getTaskFromInput())

                    findNavController().navigateUp()
                }
            }
        }
    }

    // Initial fields with task (if it is edit page)
    private fun initValues(time: PersianDate) {
        with(time) {
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
                            year = it.persianYear
                            month = it.persianMonth
                            day = it.persianDay
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
                hour = h
                minute = m
                setTimeText(h, m)
            }

        timePicker = TimePickerDialog(
            requireContext(),
            timePickerClickHandler,
            hour,
            minute,
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