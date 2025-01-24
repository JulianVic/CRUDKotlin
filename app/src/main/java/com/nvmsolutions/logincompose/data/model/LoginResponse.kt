package com.nvmsolutions.logincompose.data.model

data class LoginResponse(
    val user: User,
    val token: String
)