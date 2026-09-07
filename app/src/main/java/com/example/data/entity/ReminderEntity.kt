package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val ethYear: Int,
    val ethMonth: Int,
    val ethDay: Int,
    val gregYear: Int,
    val gregMonth: Int,
    val gregDay: Int,
    val hour: Int = 9,
    val minute: Int = 0,
    val type: String = "Reminder", // Exam, Work, Birthday, Religious, Holiday, Custom
    val repeatMode: String = "None", // None, Daily, Weekly, Monthly, Yearly
    val leadTimeMinutes: Int = 1440, // 0 (at time), 15 (15m before), 60 (1h before), 1440 (1 day before)
    val notes: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
