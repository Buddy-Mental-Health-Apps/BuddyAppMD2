package com.example.buddyapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.buddyapp.data.local.Journal
import com.example.buddyapp.data.local.JournalDao
import com.example.buddyapp.data.local.JournalRoomDatabase
import com.example.buddyapp.data.local.ResultJournal
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

    suspend fun getJournalById(journalId: Int): Journal {
        return mJournalDao.getJournalById(journalId)
    }

    fun getNewestJournalId(): LiveData<Int> {
        return mJournalDao.getNewestJournalId()
    }

    fun insertJournal(journal: Journal) {
        executorService.execute { mJournalDao.insertJournal(journal) }
    }

    fun deleteJournal(journal: Journal) {
        executorService.execute { mJournalDao.deleteJournal(journal) }
    }

    fun updateJournal(journal: Journal) {
        executorService.execute { mJournalDao.updateJournal(journal) }
    }

    suspend fun updateIsAnalyzed(journalId: Int, isAnalyzed: Boolean) {
        mJournalDao.updateIsAnalyzed(journalId, isAnalyzed)
    }

    suspend fun isResultJournalSaved(journalId: Int?): Boolean {
        return mJournalDao.isResultJournalSaved(journalId)
    }

    suspend fun insertResultJournal(resultJournal: ResultJournal) {
        mJournalDao.insertResultJournal(resultJournal)
    }

    suspend fun deleteResultJournal(id: Int) {
        mJournalDao.deleteResultJournal(id)
    }

    suspend fun getResultJournal(journalId: Int): ResultJournal {
        return mJournalDao.getResultJournal(journalId)
    }

}