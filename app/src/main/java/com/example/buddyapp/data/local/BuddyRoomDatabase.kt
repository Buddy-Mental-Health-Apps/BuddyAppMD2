package com.example.buddyapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Quiz::class, Journal::class, ResultJournal::class, Medicine::class], version = 1, exportSchema = false)
abstract class BuddyRoomDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao
    abstract fun quizDao(): QuizDao
    abstract fun medicineDao(): MedicineDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): BuddyRoomDatabase {
            if (INSTANCE == null) {
                synchronized(BuddyRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        BuddyRoomDatabase::class.java, "buddy_database")
                        .build()
                }
            }
            return INSTANCE as BuddyRoomDatabase
        }
    }
}


