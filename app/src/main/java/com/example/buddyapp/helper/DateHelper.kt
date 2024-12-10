package com.example.buddyapp.helper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateHelper {
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy | HH:mm", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun convertTimestampToDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }
}