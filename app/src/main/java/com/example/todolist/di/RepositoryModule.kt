package com.example.todolist.di

import com.example.todolist.data.repo.TaskDefaultRepository
import com.example.todolist.data.repo.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindsTaskRepository(repos: TaskDefaultRepository): TaskRepository

}