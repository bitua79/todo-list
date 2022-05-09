package com.example.todolist.util

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.todolist.R
import com.example.todolist.core.MyAutoCompleteTextView
import com.example.todolist.data.model.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import java.util.*

fun Context.showDoneTaskDialog(task: Task, doneTask: (t: Task) -> Unit) {
    MaterialAlertDialogBuilder(this)
        .setIcon(R.drawable.ic_error)
        .setTitle(getString(R.string.msg_done))
        .setCancelable(true)
        .setPositiveButton(
            getString(R.string.msg_confirm_done)
        ) { _, _ -> doneTask(task) }
        .setNegativeButton(
            getString(R.string.msg_reject_done)
        ) { _, _ -> }
        .show()
}

fun Fragment.initializePersianDatePicker(): PersianDatePickerDialog {
    return PersianDatePickerDialog(requireContext())
        .setPositiveButtonString(getString(R.string.label_confirm))
        .setNegativeButton(getString(R.string.label_escape))
        .setTodayButton(getString(R.string.label_today))
        .setTodayButtonVisible(true)
        .setMinYear(1396)//TODO use constant
        .setMaxYear(PersianDatePickerDialog.THIS_YEAR + 1)
        .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.color_on_surface))
        .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
        .setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_surface))
        .setPickerBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.color_surface
            )
        )
        .setTitleColor(ContextCompat.getColor(requireContext(), R.color.color_on_surface))
        .setShowInBottomSheet(true)
}

fun PersianDatePickerDialog.setDatePickerClickHandler(
    completeTextView: MyAutoCompleteTextView,
    action: (PersianPickerDate?) -> Unit,
    removeError: () -> Unit
) {
    setListener(object : PersianPickerListener {
        override fun onDateSelected(persianPickerDate: PersianPickerDate?) {
            removeError()
            val label = persianPickerDate.getDatePickerDate()

            completeTextView.setText(label, false)
            action(persianPickerDate)
        }

        override fun onDismissed() {}
    })

    show()
}

fun PersianPickerDate?.getDatePickerDate(): String {
    this?.let {
        return String.format(
            Locale.getDefault(),
            "%d/%d/%d",
            it.persianYear,
            it.persianMonth,
            it.persianDay
        )
    }
    return ""
}

fun TextView.isEmpty(textInputLayout: TextInputLayout): Boolean {
    if (text.isNullOrEmpty()) {
        textInputLayout.error = "این فیلد نمی تواند خالی باشد"
        return true
    } else {
        textInputLayout.isErrorEnabled = false
        textInputLayout.error = null
    }
    return false
}
