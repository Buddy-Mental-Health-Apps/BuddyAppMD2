package com.example.buddyapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertJournal(journal: Journal)

    @Update
    fun updateJournal(journal: Journal)

    @Delete
    fun deleteJournal(journal: Journal)

    @Query("SELECT * from journal ORDER BY id DESC")
    fun getAllJournal(): LiveData<List<Journal>>

    @Query("SELECT * from journal where id = :id")
    suspend fun getJournalById(id: Int): Journal

    @Query("SELECT id from journal ORDER BY initialTimestamp DESC LIMIT 1")
    fun getNewestJournalId(): LiveData<Int>

    @Query("UPDATE journal SET isAnalyzed = :isAnalyzed WHERE id = :id")
    suspend fun updateIsAnalyzed(id: Int, isAnalyzed: Boolean)

    @Query("SELECT MAX(initialTimestamp) FROM journal")
    suspend fun getLastJournalDate(): Long?

    @Query("SELECT EXISTS(SELECT 1 FROM result_journal WHERE id = :id)")
    suspend fun isResultJournalSaved(id: Int?): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertResultJournal(resultJournal: ResultJournal)

    @Query("DELETE FROM result_journal WHERE id = :id")
    suspend fun deleteResultJournal(id: Int)

    @Query("SELECT * from result_journal where id = :id")
    suspend fun getResultJournal(id: Int): ResultJournal

    @Insert
    suspend fun insertJournalHistory(journalEntry: JournalEntry)

    @Query("SELECT * FROM journal_entries ORDER BY date ASC")
    fun getAllJournalsHistory(): LiveData<List<JournalEntry>>
}