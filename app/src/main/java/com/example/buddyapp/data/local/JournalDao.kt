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
    fun insert(journal: Journal)

    @Update
    fun update(journal: Journal)

    @Delete
    fun delete(journal: Journal)

    @Query("SELECT * from journal ORDER BY id ASC")
    fun getAllJournal(): LiveData<List<Journal>>

    @Query("SELECT * from journal where id = :id")
    fun getJournalById(id: String): LiveData<Journal>

}