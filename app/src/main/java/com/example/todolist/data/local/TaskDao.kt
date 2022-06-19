package com.example.todolist.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.data.model.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("UPDATE tbl_tasks SET deadLine= :deadline, name=:name WHERE id=:id")
    fun editTask(id: Int, name: String, deadline: Long)

    @Query("SELECT id FROM tbl_tasks WHERE name=:name AND subject=:subject")
    fun getTaskId(name: String, subject: String): Int

    @Query("SELECT * FROM tbl_tasks WHERE name LIKE '%' || :query || '%'")
    fun getTasksBQuery(query: String): LiveData<List<Task>>

    @Query("SELECT * FROM tbl_tasks")
    fun getAllTasks(): LiveData<List<Task>>
}