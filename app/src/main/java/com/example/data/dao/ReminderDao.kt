package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders ORDER BY ethYear ASC, ethMonth ASC, ethDay ASC, hour ASC, minute ASC")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE isCompleted = 0 ORDER BY ethYear ASC, ethMonth ASC, ethDay ASC, hour ASC, minute ASC")
    fun getActiveReminders(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE ethYear = :year AND ethMonth = :month ORDER BY ethDay ASC")
    fun getRemindersForMonth(year: Int, month: Int): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE ethYear = :year AND ethMonth = :month AND ethDay = :day")
    fun getRemindersForDay(year: Int, month: Int, day: Int): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE title LIKE '%' || :query || '%' OR notes LIKE '%' || :query || '%'")
    fun searchReminders(query: String): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteReminderById(id: Long)

    @Query("UPDATE reminders SET isCompleted = :completed WHERE id = :id")
    suspend fun setReminderCompleted(id: Long, completed: Boolean)
}
