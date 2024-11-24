package com.example.buddyapp.data.ds

import android.util.Log
import com.example.buddyapp.data.api.ApiService
import com.example.buddyapp.data.api.RegisterResponse

class RegisterRepository(private val apiService: ApiService) {

    suspend fun registerUser(register: Register): RegisterResponse {
        return try {
            Log.d("RegisterRepository", "Mengirim request untuk: ${register.email}")
            val response = apiService.register(
                name = register.name,
                email = register.email,
                password = register.password
            )
            Log.d("RegisterRepository", "Respons API: ${response.message}")
            response
        } catch (e: Exception) {
            Log.e("RegisterRepository", "Error saat melakukan register: ${e.message}")
            RegisterResponse(error = true, message = "Gagal melakukan register: ${e.message}")
        }
    }
}