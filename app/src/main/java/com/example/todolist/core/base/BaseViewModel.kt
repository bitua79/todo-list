package com.example.todolist.core.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel(

) : ViewModel() {

    private val refreshing = MutableLiveData<Boolean>()

    fun refresh() {
        refreshing.value = true
    }

}