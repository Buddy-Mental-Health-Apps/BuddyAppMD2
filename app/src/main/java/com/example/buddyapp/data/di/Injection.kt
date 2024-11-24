package com.example.buddyapp.data.di

import android.content.Context
import com.example.buddyapp.data.api.ApiConfig
import com.example.buddyapp.data.api.ApiService
import com.example.buddyapp.data.ds.UserPreference
import com.example.buddyapp.data.ds.UserRepository
import com.example.buddyapp.data.ds.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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

    // Fungsi tambahan jika diperlukan untuk mengambil ApiService dengan autentikasi bearer token
    suspend fun fetchApiServiceAndGetRepository(pref: UserPreference): ApiService {
        val user = pref.getSession().first()
        return ApiConfig.getApiServices("Bearer ${user.token}")
    }
}
