package com.example.buddyapp.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "journal")
@Parcelize
data class Journal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "image")
    var image: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "initialTimestamp")
    var initialTimestamp: Long? = null,

    @ColumnInfo(name = "timestamp")
    var timestamp: String? = null,

    @ColumnInfo(name = "isAnalyzed")
    var isAnalyzed: Boolean = false
) : Parcelable

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String?,
    val title: String?
)