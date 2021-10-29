package com.example.cafe.retrofit.response.login

data class LoginResponse(
    val `data`: Login,
    val error: Boolean,
    val message: String
)