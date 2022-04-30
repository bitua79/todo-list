/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.todolist.appinitializer

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.todolist.core.settings.AppPreferences
import com.example.todolist.core.settings.Language
import com.example.todolist.core.settings.Theme
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Named


class AppPreferencesImpl @Inject constructor(
    @Named("app")
    val sharedPreferences: SharedPreferences
) : AppPreferences {

    private val defaultThemeValue = Theme.DARK.value

    private val defaultLanguageValue = Language.Fa.value

    private val preferenceKeyChangedFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        preferenceKeyChangedFlow.tryEmit(key)
    }

    companion object {
        const val KEY_THEME = "pref_theme"
        const val KEY_LANG = "pref_lang"
    }

    override fun setup() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override var theme: Theme
        get() = getThemeForStorageKey(sharedPreferences.getString(KEY_THEME, defaultThemeValue)!!)
        set(value) = sharedPreferences.edit {
            putString(KEY_THEME, value.value)
        }

    override var language: Language
        get() = getLanguageForStorageKey(sharedPreferences.getString(KEY_LANG, defaultLanguageValue)!!)
        set(value) = sharedPreferences.edit {
            putString(KEY_LANG, value.value)
        }

    override fun observeTheme(): Flow<Theme> {
        return preferenceKeyChangedFlow
            // Emit on start so that we always send the initial value
            .onStart { emit(KEY_THEME) }
            .filter { it == KEY_THEME }
            .map { theme }
            .distinctUntilChanged()
    }

    override fun observeLanguage(): Flow<Language> {
        return preferenceKeyChangedFlow
            // Emit on start so that we always send the initial value
//            .onStart { emit(KEY_LANG) }
            .filter { it == KEY_LANG }
            .map { language }
            .distinctUntilChanged()
    }

    private fun getThemeForStorageKey(value: String) = when (value) {
        Theme.DARK.value -> {
            Theme.DARK
        }

        else -> {
            Theme.LIGHT
        }
    }

    private fun getLanguageForStorageKey(value: String) = when (value) {
        Language.En.value -> {
            Language.En
        }

        else -> {
            Language.Fa
        }
    }
}
