package com.example.todolist.core

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("getStringRes")
fun getStringResource(view: TextView, id: Int) {
    view.text = view.context.getString(id)
}

@BindingAdapter("setNullableText")
fun setNullableText(view: TextInputEditText, text: String?) {
    text?.let {
        view.setText(text)
    }
}

@BindingAdapter("setNullableText")
fun setNullableText(view: MyAutoCompleteTextView, text: String?) {
    text?.let {
        view.setText(text)
    }
}

@BindingAdapter("setNullableText")
fun setNullableText(view: MyAutoCompleteTextView, text: Int?) {
    text?.let {
        try {
            view.setText(view.context.getString(text))
        } catch (e: Throwable) {

        }
    }
}