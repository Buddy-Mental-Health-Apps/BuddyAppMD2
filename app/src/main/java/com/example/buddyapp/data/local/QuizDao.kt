package com.example.buddyapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(quiz: Quiz)

    @Delete
    fun delete(quiz: Quiz)

    @Query("SELECT * from quiz ORDER BY id ASC")
    fun getAllQuiz(): LiveData<List<Quiz>>

    @Query("SELECT * FROM quiz WHERE date = :date AND title = :title LIMIT 1")
    fun getQuizByDateAndTitle(date: String, title: String): Quiz?

    @Query("SELECT * FROM quiz WHERE id = :id LIMIT 1")
    fun getQuizById(id: Int): Quiz?

}