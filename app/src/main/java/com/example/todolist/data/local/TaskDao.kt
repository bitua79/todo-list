package com.example.todolist.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.todolist.data.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM tbl_tasks")
    fun getAllTasks(): LiveData<List<Task>>
}