package com.example.todolist.di

import android.content.Context
import android.content.SharedPreferences
import com.example.todolist.appinitializer.AppPreferencesImpl
import com.example.todolist.core.settings.AppPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun providePersianDate() = PersianDate()

    @Provides
    fun providePersianDateFormat() = PersianDateFormat("Ymd")

    @Provides
    fun provideCalendar(): Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"))

    @InstallIn(SingletonComponent::class)
    @Module
    internal abstract class SettingsModuleBinds {
        @Singleton
        @Binds
        abstract fun providePreferences(bind: AppPreferencesImpl): AppPreferences
    }


    @InstallIn(SingletonComponent::class)
    @Module
    internal object SettingsModule {
        @Named("app")
        @Provides
        @Singleton
        fun provideAppSharedPreferences(
            @ApplicationContext context: Context
        ): SharedPreferences {
            return context.getSharedPreferences("signal_demo_prefs", Context.MODE_PRIVATE)
        }
    }
}