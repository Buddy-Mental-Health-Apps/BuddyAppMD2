package com.example.buddyapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.impl.WorkDatabaseMigrations.MIGRATION_1_2

@Database(entities = [Quiz::class, Journal::class], version = 2, exportSchema = false)
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
                        JournalRoomDatabase::class.java, "history_database")
                        .addMigrations(com.example.buddyapp.data.local.MIGRATION_1_2)
                        .build()
                }
            }
            return INSTANCE as JournalRoomDatabase
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Membuat tabel 'quiz' dengan kolom yang sesuai
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `quiz` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                `title` TEXT, 
                `description` TEXT, 
                `date` TEXT     
            )
        """)
    }
}


