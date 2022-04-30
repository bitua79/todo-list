package com.example.todolist.di

import com.example.todolist.core.AppDatabase
import com.example.todolist.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
object DaoModule {

    @Provides
    @Singleton
    fun taskDao(db: AppDatabase): TaskDao = db.taskDao()

}