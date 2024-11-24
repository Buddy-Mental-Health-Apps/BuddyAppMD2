package com.example.buddyapp.data.ds

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = user.userId
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
            preferences[TOKEN_EXP_KEY] = user.tokenExp
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                email = preferences[EMAIL_KEY] ?: "",
                token = preferences[TOKEN_KEY] ?: "",
                isLogin = preferences[IS_LOGIN_KEY] ?: false,
                userId = preferences[USER_ID_KEY] ?: "",
                tokenExp = preferences[TOKEN_EXP_KEY] ?: ""
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences -> preferences.clear() }
    }

    suspend fun setFirstTimeStatus() {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME_KEY] = false
        }
    }

    fun isFirstTime(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_FIRST_TIME_KEY] ?: true
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val USER_ID_KEY = stringPreferencesKey("userId")
        private val TOKEN_EXP_KEY = stringPreferencesKey("tokenExp")
        private val IS_FIRST_TIME_KEY = booleanPreferencesKey("is_first_time")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
