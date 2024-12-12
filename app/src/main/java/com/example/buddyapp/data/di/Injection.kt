package com.example.buddyapp.data.di

import android.content.Context
import com.example.buddyapp.data.api.ApiConfig
import com.example.buddyapp.data.api.ApiService
import com.example.buddyapp.data.ds.UserPreference
import com.example.buddyapp.data.ds.UserRepository
import com.example.buddyapp.data.ds.dataStore

object Injection {

    // Menyediakan UserRepository menggunakan UserPreference
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    // Menyediakan ApiService
    fun provideApiService(): ApiService {
        return ApiConfig.getApiService()
    }
}
