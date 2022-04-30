package com.example.todolist.appinitializer

import android.app.Application
import com.example.todolist.core.settings.AppPreferences
import com.example.todolist.util.AppInitializer
import javax.inject.Inject

class PreferencesInitializer @Inject constructor(
    private val prefs: AppPreferences
) : AppInitializer {
    override fun init(application: Application) {
        prefs.setup()
    }
}
