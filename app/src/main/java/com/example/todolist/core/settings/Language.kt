package com.example.todolist.core.settings

import java.util.*

enum class Language(val value: String) {
    Fa("fa"),
    En("en");


    fun getLocale() = Locale(value)

    companion object {
        fun findByValue(value: String): Language {
            return enumValues<Language>().firstOrNull { it.value == value } ?: Fa
        }
    }
}