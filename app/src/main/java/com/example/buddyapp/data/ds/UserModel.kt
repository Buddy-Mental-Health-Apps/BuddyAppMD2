package com.example.buddyapp.data.ds

data class UserModel(
    val userId: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false,
    val tokenExp: String,
    val name: String
)