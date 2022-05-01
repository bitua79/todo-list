package com.example.todolist.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.data.model.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("UPDATE tbl_tasks SET deadLine= :deadlineS WHERE name=:name")
    fun edit(name: String, deadlineS: Long)

    @Query("SELECT * FROM tbl_tasks")
    fun getAllTasks(): LiveData<List<Task>>
}