package com.example.todolist

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.example.todolist.appinitializer.AppInitializers
import com.example.todolist.appinitializer.AppPreferencesImpl
import com.example.todolist.core.settings.LocaleHelper
import com.example.todolist.di.AppModule
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(){
    @Inject
    lateinit var initializers: AppInitializers

    override fun onCreate() {
        super.onCreate()
        initializers.init(this)
        LocaleHelper.localizeContext(this, resources.configuration)

    }

    override fun attachBaseContext(base: Context) {
        LocaleHelper.loadLocalePref(AppPreferencesImpl(AppModule.SettingsModule.provideAppSharedPreferences(base)))
        super.attachBaseContext(base)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.localizeContext(this, newConfig, true)
    }

}