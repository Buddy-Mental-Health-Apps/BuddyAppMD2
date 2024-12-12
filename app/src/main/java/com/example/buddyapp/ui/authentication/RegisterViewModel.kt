package com.example.buddyapp.ui.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buddyapp.data.ds.Register
import com.example.buddyapp.data.ds.RegisterRepository
import kotlinx.coroutines.launch
import com.example.buddyapp.data.api.response.RegisterResponse

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> get() = _registerResponse

    fun registerUser(register: Register) {
        viewModelScope.launch {
            try {
                Log.d("RegisterViewModel", "Memulai proses registrasi untuk: ${register.email}")
                val response = registerRepository.registerUser(register)
                _registerResponse.value = response
                Log.d("RegisterViewModel", "Respons diterima: ${response.message}")
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Error: ${e.message}")
                _registerResponse.value = RegisterResponse(
                    error = true,
                    message = "Terjadi kesalahan saat register: ${e.message}"
                )
            }
        }
    }
}
