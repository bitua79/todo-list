package com.example.todolist.util

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.todolist.R
import com.google.android.material.textfield.TextInputLayout
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import java.util.*

inline fun <T> Flow<T>.collectOnFragment(fragment: Fragment, crossinline onCollect: (T) -> Unit) {
    fragment.lifecycleScope.launchWhenStarted {
        this@collectOnFragment.collectLatest {
            onCollect(it)
        }
    }
}

inline fun <T> Flow<T>.collectOnActivity(
    activity: AppCompatActivity,
    crossinline onCollect: (T) -> Unit
) {
    activity.lifecycleScope.launchWhenStarted {
        this@collectOnActivity.collectLatest {
            onCollect(it)
        }
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
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

// Initial persian date picker and Set init time on deadline
fun Fragment.initializePersianDatePicker(timeStamp: Long?, thisYear: Int): PersianDatePickerDialog {

    // Set date settings
    val datePicker = PersianDatePickerDialog(requireContext())
        .setMinYear(1396)
        .setMaxYear(thisYear + 1)
    timeStamp?.let { datePicker.setInitDate(timeStamp) }

    // Theming
    datePicker.setActionTextColor(getColor(requireContext(), R.color.color_on_surface))
        .setPickerBackgroundColor(getColor(requireContext(), R.color.color_surface))
        .setBackgroundColor(getColor(requireContext(), R.color.color_surface))
        .setTitleColor(getColor(requireContext(), R.color.color_on_surface))

    // Set Ui
    datePicker.setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
        .setPositiveButtonString(getString(R.string.label_confirm))
        .setNegativeButton(getString(R.string.label_escape))
        .setTodayButton(getString(R.string.label_today))
        .setTodayButtonVisible(true)

    return datePicker
}

fun getPersianDateString(y: Int, m: Int, d: Int): String {
    return String.format(
        Locale.getDefault(),
        "%2d/%2d/%2d",
        y,
        m,
        d
    ).replace(" ", getZeroInDefaultLocale())
}

fun getColor(context: Context, id: Int): Int {
    return ContextCompat.getColor(context, id)
}
