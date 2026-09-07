package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val language: String = "am", // "am" = Amharic, "en" = English
    val calendarPreference: String = "ethiopian_first", // "ethiopian_first", "gregorian_first"
    val firstDayOfWeek: Int = 0, // 0 = Sunday (እሑድ), 1 = Monday (ሰኞ)
    val useGeezNumerals: Boolean = false,
    val themeMode: String = "system", // "system", "light", "dark"
    val showHolidays: Boolean = true,
    val enableNotifications: Boolean = true
)
