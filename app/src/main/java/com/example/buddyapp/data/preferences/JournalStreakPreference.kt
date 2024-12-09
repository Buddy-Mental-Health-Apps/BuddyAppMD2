package com.example.buddyapp.data.preferences

import android.content.Context
import android.content.SharedPreferences

class JournalStreakPreference(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("JournalStreakPrefs", Context.MODE_PRIVATE)

    fun getCurrentStreak(): Int {
        return preferences.getInt("current_streak", 0)
    }

    fun saveCurrentStreak(streak: Int) {
        preferences.edit().putInt("current_streak", streak).apply()
    }

    fun getHighestStreak(): Int {
        return preferences.getInt("highest_streak", 0)
    }

    fun saveHighestStreak(streak: Int) {
        preferences.edit().putInt("highest_streak", streak).apply()
    }

    fun getLastUpdatedDate(): Long {
        return preferences.getLong("last_updated_date", 0L)
    }

    fun saveLastUpdatedDate(dateMillis: Long) {
        preferences.edit().putLong("last_updated_date", dateMillis).apply()
    }
}