package com.example.buddyapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.buddyapp.data.local.Journal
import com.example.buddyapp.data.local.JournalDao
import com.example.buddyapp.data.local.BuddyRoomDatabase
import com.example.buddyapp.data.local.JournalEntry
import com.example.buddyapp.data.local.JournalStreak
import com.example.buddyapp.data.local.ResultJournal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class JournalRepository(application: Application) {
    private val mJournalDao: JournalDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = BuddyRoomDatabase.getDatabase(application)
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

    val allJournalsHistory: LiveData<List<JournalEntry>> = mJournalDao.getAllJournalsHistory()

    suspend fun insertJournalHistory(journalEntry: JournalEntry) {
        mJournalDao.insertJournalHistory(journalEntry)
    }

    suspend fun writeJournal(date: String) {
        val previousJournal = mJournalDao.getStreaksForDate(date)

        if (previousJournal == null || !previousJournal.isJournaled) {
            val currentStreak = 1
            val journal = JournalStreak(date, isJournaled = true, currentStreak = currentStreak)
            mJournalDao.insertOrUpdateJournal(journal)
        }
    }

    suspend fun getStreakData(date: String): JournalStreak {
        val streakData: JournalStreak
        val convertedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
        val lastStreak = mJournalDao.getLastStreak() ?: return JournalStreak(
            date,
            isJournaled = false,
            currentStreak = 0
        )
        val lastStreakDate = LocalDate.parse(lastStreak.date, DateTimeFormatter.ISO_DATE)
        val daysDifference = ChronoUnit.DAYS.between(lastStreakDate, convertedDate)

        val response = mJournalDao.getStreaksForDate(date)
        streakData = if (response == null && daysDifference == 1L) {
            lastStreak
        } else response ?: JournalStreak(date, isJournaled = false, currentStreak = 0)
        return streakData
    }

    suspend fun getHighestStreak(): Int {
        return mJournalDao.getHighestStreak()
    }
}