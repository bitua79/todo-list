

package com.example.todolist.core.settings

import kotlinx.coroutines.flow.Flow

interface AppPreferences {

    fun setup()

    var theme: Theme
    fun observeTheme(): Flow<Theme>

    var language: Language
    fun observeLanguage(): Flow<Language>
}
