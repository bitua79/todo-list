package com.example.todolist.data.model

import android.content.Context
import com.example.todolist.R

enum class Priority(val value: Int, val title: Int) {
    High(3, R.string.label_high_priority),
    Medium(2, R.string.label_medium_priority),
    Low(1, R.string.label_low_priority),
    Done(0, R.string.label_done);

    companion object {
        fun enumValueOfTitle(s: String, context: Context): Priority {
            for (i in values()) {
                if (context.getString(i.title) == s) return i
            }
            // default priority is low
            return Low
        }
    }
}