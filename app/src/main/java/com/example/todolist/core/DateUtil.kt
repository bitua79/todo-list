package com.example.todolist.core

import android.content.Context
import com.example.todolist.R
import saman.zamani.persiandate.PersianDate
import java.util.*
import javax.inject.Inject

class DateUtil @Inject constructor(
    private val persianDate: PersianDate,
    private val calendar: Calendar
) {
    init {
        resetTime()
    }

    // Set persian date to today
    private fun resetTime() {
        calendar.time = Date()
        resetTime(calendar)
    }

    private fun resetTime(cal: Calendar) {
        cal.time = Date()
        persianDate.initGrgDate(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            cal.get(Calendar.SECOND)
        )
    }

    private fun getTodayDate(): PersianDate {
        resetTime()
        return persianDate
    }

    fun getRemainTime(deadLine: PersianDate, context: Context): String {
        val today = getTodayDate()
        return calculateDiffDate(today, deadLine, context)
    }

    private fun calculateDiffDate(
        date1: PersianDate,
        date2: PersianDate,
        context: Context
    ): String {
        var diffSec = date2.second - date1.second
        var diffMin = date2.minute - date1.minute
        var diffHour = date2.hour - date1.hour
        var diffDay = date2.shDay - date1.shDay
        var diffMonth = date2.shMonth - date1.shMonth
        var diffYear = date2.shYear - date1.shYear

        if (diffSec < 0) {
            diffSec += 60
            diffMin--
        }

        if (diffMin < 0) {
            diffMin += 60
            diffHour--
        }

        if (diffHour < 0) {
            diffHour += 24
            diffDay--
        }
        if (diffDay < 0) {
            diffDay += 30
            diffMonth--
        }
        if (diffMonth < 0) {
            diffMonth += 12
            diffYear--
        }

        diffDay += diffMonth * 30 + diffYear*365

        return when {
            diffYear < 0 -> {
                context.getString(R.string.deadline_is_passed)
            }
            diffDay == 0 -> {
                context.getString(R.string.remain_time_hour, diffHour.twoDigit(), diffMin.twoDigit())

            }
            else -> {
                context.getString(R.string.remain_time_day, diffDay.twoDigit(), diffHour.twoDigit())
            }
        }

    }

}

fun getDate(time: Long) = PersianDate(time)

fun getPersianDate(
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int,
    second: Int
): Long {
    val d = PersianDate().initJalaliDate(
        year,
        month,
        day,
        hour,
        minute,
        second,
    )
    // add hour ad minute time millis
    return d.time + (hour * 3600000 + minute * 60000)
}

fun PersianDate.toDateWithSlash() =
    "${(shYear).twoDigit()}/${shMonth.twoDigit()}/${shDay.twoDigit()}"

//TODO FIX 0
fun Int.twoDigit(): String {     // To handle one digit fields => 0:0 -> 00:00
//    return String.format(Locale.US, "%2d", this).replace(" ", "0")
//    return String.format(Locale.getDefault(), "%2d", this).replace(" ", "0")
    return String.format(Locale.getDefault(), "%d", this)
}