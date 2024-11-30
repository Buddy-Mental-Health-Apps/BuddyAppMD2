package com.example.buddyapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.buddyapp.data.local.Journal
import com.example.buddyapp.data.local.JournalDao
import com.example.buddyapp.data.local.JournalRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class JournalRepository(application: Application) {
    private val mJournalDao: JournalDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = JournalRoomDatabase.getDatabase(application)
        mJournalDao = db.journalDao()
    }

    fun getAllJournal(): LiveData<List<Journal>> = mJournalDao.getAllJournal()

    suspend fun insert(journal: Journal) {
        executorService.execute { mJournalDao.insert(journal) }
    }

    suspend fun delete(journal: Journal) {
        executorService.execute { mJournalDao.delete(journal) }
    }

    suspend fun update(journal: Journal) {
        executorService.execute { mJournalDao.update(journal) }
    }

    fun getJournalById(id: String): LiveData<Journal> = mJournalDao.getJournalById(id)
}