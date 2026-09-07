package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.ReminderDao
import com.example.data.dao.UserSettingsDao
import com.example.data.entity.ReminderEntity
import com.example.data.entity.UserSettingsEntity

@Database(
    entities = [ReminderEntity::class, UserSettingsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun userSettingsDao(): UserSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "zemen_calendar.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
