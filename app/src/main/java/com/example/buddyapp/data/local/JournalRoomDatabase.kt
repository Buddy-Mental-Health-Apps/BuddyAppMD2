package com.example.buddyapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.impl.WorkDatabaseMigrations.MIGRATION_1_2

@Database(entities = [Quiz::class, Journal::class, ResultJournal::class], version = 1, exportSchema = false)
abstract class JournalRoomDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao
    abstract fun quizDao(): QuizDao

    companion object {
        @Volatile
        private var INSTANCE: JournalRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): JournalRoomDatabase {
            if (INSTANCE == null) {
                synchronized(JournalRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        JournalRoomDatabase::class.java, "journal_database")
                        .build()
                }
            }
            return INSTANCE as JournalRoomDatabase
        }
    }
}


