package com.nvmsolutions.logincompose.data.model

data class RegisterResponse(
    val user: User,
    val token: String
)