package com.nvmsolutions.logincompose.data.model

data class User(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val groups: List<String>,
    val friends: List<String>
)