package com.example.todolist.core

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import com.example.todolist.R

import com.google.android.material.textfield.MaterialAutoCompleteTextView


class MyAutoCompleteTextView @JvmOverloads constructor(context: Context,
                                                       attributeSet: AttributeSet? = null)
    : MaterialAutoCompleteTextView(context, attributeSet) {
    private var isCallingSetText = false

    override fun setText(text: CharSequence?, type: BufferType?) {
        //  override method to save state after configuration change
        if (isCallingSetText || inputType != EditorInfo.TYPE_NULL) {
            super.setText(text, type)
        } else {
            isCallingSetText = true
            setText(text, false)
            isCallingSetText = false
        }
    }
}