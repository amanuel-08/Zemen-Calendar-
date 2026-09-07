package com.example.data.repository

import com.example.calendar.EthiopianCalendarEngine
import com.example.calendar.EthiopianDate
import com.example.calendar.GregorianDate
import com.example.calendar.HolidaysData
import com.example.calendar.HolidayInstance
import com.example.data.dao.ReminderDao
import com.example.data.dao.UserSettingsDao
import com.example.data.entity.ReminderEntity
import com.example.data.entity.UserSettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalendarRepository(
    private val reminderDao: ReminderDao,
    private val userSettingsDao: UserSettingsDao
) {
    val allReminders: Flow<List<ReminderEntity>> = reminderDao.getAllReminders()
    val activeReminders: Flow<List<ReminderEntity>> = reminderDao.getActiveReminders()
    val settings: Flow<UserSettingsEntity> = userSettingsDao.getSettings().map {
        it ?: UserSettingsEntity()
    }

    fun getRemindersForMonth(year: Int, month: Int): Flow<List<ReminderEntity>> {
        return reminderDao.getRemindersForMonth(year, month)
    }

    fun getRemindersForDay(year: Int, month: Int, day: Int): Flow<List<ReminderEntity>> {
        return reminderDao.getRemindersForDay(year, month, day)
    }

    suspend fun saveReminder(
        title: String,
        ethDate: EthiopianDate,
        gregDate: GregorianDate,
        hour: Int,
        minute: Int,
        type: String,
        repeatMode: String,
        leadTimeMinutes: Int,
        notes: String
    ): Long {
        val entity = ReminderEntity(
            title = title,
            ethYear = ethDate.year,
            ethMonth = ethDate.month,
            ethDay = ethDate.day,
            gregYear = gregDate.year,
            gregMonth = gregDate.month,
            gregDay = gregDate.day,
            hour = hour,
            minute = minute,
            type = type,
            repeatMode = repeatMode,
            leadTimeMinutes = leadTimeMinutes,
            notes = notes
        )
        return reminderDao.insertReminder(entity)
    }

    suspend fun addHolidayAsReminder(holiday: HolidayInstance): Long {
        return reminderDao.insertReminder(
            ReminderEntity(
                title = holiday.nameAm,
                ethYear = holiday.ethDate.year,
                ethMonth = holiday.ethDate.month,
                ethDay = holiday.ethDate.day,
                gregYear = holiday.gregDate.year,
                gregMonth = holiday.gregDate.month,
                gregDay = holiday.gregDate.day,
                hour = 8,
                minute = 0,
                type = "Holiday",
                repeatMode = "Yearly",
                leadTimeMinutes = 1440,
                notes = "${holiday.nameEn} - ${holiday.descriptionAm}"
            )
        )
    }

    suspend fun toggleReminderComplete(id: Long, isCompleted: Boolean) {
        reminderDao.setReminderCompleted(id, isCompleted)
    }

    suspend fun deleteReminder(id: Long) {
        reminderDao.deleteReminderById(id)
    }

    suspend fun updateSettings(settings: UserSettingsEntity) {
        userSettingsDao.saveSettings(settings)
    }

    fun searchReminders(query: String): Flow<List<ReminderEntity>> {
        return reminderDao.searchReminders(query)
    }
}
