package com.example.todolist.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.data.model.Priority
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "tbl_tasks"
)
data class Task(
    @PrimaryKey
    val name: String,
    val subject: String,
    val deadLine: Long,
    val remainTime: String? = null,
    val priority: Priority,
    val isDone: Boolean = false
) : Parcelable

