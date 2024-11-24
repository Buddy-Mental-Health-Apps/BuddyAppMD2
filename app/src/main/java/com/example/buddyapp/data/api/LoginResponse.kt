package com.example.buddyapp.data.api

data class LoginResponse(
    val loginResult: LoginResult? = null,
    val error: Boolean? = null,
    val message: String? = null
)

data class LoginResult(
    val userId: String? = null,
    val email: String? = null,
    val token: String? = null,
    val tokenExpiration: String? = null
)