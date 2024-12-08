package com.example.buddyapp.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "result_journal")
@Parcelize
data class ResultJournal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "result_id")
    var resultId: Int = 0,

    @ColumnInfo(name = "id")
    var id: Int? = 0,

    @ColumnInfo(name = "positive_percentage")
    var positivePercentage: String,

    @ColumnInfo(name = "negative_percentage")
    var negativePercentage: String,

    @ColumnInfo(name = "positive_count")
    var positiveCount: Int,

    @ColumnInfo(name = "negative_count")
    var negativeCount: Int,

    @ColumnInfo(name = "positive_words")
    var positiveWords: String,

    @ColumnInfo(name = "negative_words")
    var negativeWords: String
) : Parcelable