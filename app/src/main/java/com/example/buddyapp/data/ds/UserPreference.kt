package com.example.buddyapp.data.ds

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
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
            preferences[NAME_KEY] = user.name
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                email = preferences[EMAIL_KEY] ?: "",
                token = preferences[TOKEN_KEY] ?: "",
                isLogin = preferences[IS_LOGIN_KEY] ?: false,
                userId = preferences[USER_ID_KEY] ?: "",
                tokenExp = preferences[TOKEN_EXP_KEY] ?: "",
                name = preferences[NAME_KEY] ?: ""
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(EMAIL_KEY)
            preferences.remove(TOKEN_KEY)
            preferences.remove(IS_LOGIN_KEY)
            preferences.remove(USER_ID_KEY)
            preferences.remove(TOKEN_EXP_KEY)
        }
    }

    suspend fun setNotFirstTimeStatus() {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME_KEY] = false
        }
    }

    fun isFirstTime(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_FIRST_TIME_KEY] ?: true
        }
    }

    fun getName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[NAME_KEY] ?: "Pengguna"
        }
    }

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
        }
    }

    // Menyimpan waktu login
    suspend fun setLoginTime(time: Long) {
        dataStore.edit { preferences ->
            preferences[LOGIN_TIME_KEY] = time
        }
    }

    // Mendapatkan waktu login
    fun getLoginTime(): Flow<Long> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_TIME_KEY] ?: 0L
        }
    }

    // Menyimpan status login
    suspend fun setLoginStatus(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    // Mendapatkan status login
    fun getLoginStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN_KEY] ?: false
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
        private val NAME_KEY = stringPreferencesKey("name")

        // Menambahkan key untuk waktu login dan status login
        private val LOGIN_TIME_KEY = longPreferencesKey("login_time")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
