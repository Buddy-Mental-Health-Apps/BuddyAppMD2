package com.example.buddyapp.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buddyapp.data.api.ApiService
import com.example.buddyapp.data.api.LoginResponse
import com.example.buddyapp.data.ds.UserModel
import com.example.buddyapp.data.ds.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository,
    private val apiService: ApiService,
) : ViewModel() {

    private val _loginResult = MutableStateFlow<Result<UserModel>?>(null)
    val loginResult: StateFlow<Result<UserModel>?> = _loginResult

    private val _apiMessage = MutableStateFlow<String?>(null)
    val apiMessage: StateFlow<String?> = _apiMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response: LoginResponse = apiService.login(email, password)

                if (!response.error!!) {
                    response.loginResult?.let { loginResult ->
                        val userModel = UserModel(
                            userId = loginResult.userId ?: "",
                            email = email,
                            token = loginResult.token ?: "",
                            tokenExp = loginResult.tokenExpiration ?: "",
                            isLogin = true
                        )
                        repository.saveSession(userModel)
                        _loginResult.value = Result.success(userModel)

                        _apiMessage.value = response.message ?: "Login sukses"


                    } ?: run {
                        _loginResult.value = Result.failure(Exception("Login result kosong"))
                    }
                } else {
                    _loginResult.value = Result.failure(
                        Exception(
                            response.message ?: "Terjadi kesalahan saat login"
                        )
                    )
                    _apiMessage.value = response.message ?: "Terjadi kesalahan"
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
                _apiMessage.value = "Gagal menghubungi server"
            }
        }
    }
}