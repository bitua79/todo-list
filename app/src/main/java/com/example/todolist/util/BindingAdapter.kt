package com.example.todolist.util

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("getStringRes")
fun getStringResource(view: TextView, id: Int) {
    view.text = view.context.getString(id)
}

@BindingAdapter("setNullableText")
fun setNullableText(view: TextView, text: String?) {
    text?.let {
        view.text = text
    }
}

@BindingAdapter("setNullableText")
fun setNullableText(view: TextView, text: Int?) {
    text?.let {
        try {
            view.text = view.context.getString(text)
        } catch (e: Throwable) {
            view.text = ""
        }
    }
}