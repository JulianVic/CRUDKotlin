package com.nvmsolutions.logincompose.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val id: String,
    val name: String,
    val username: String,
    val email: String,
    val groups: List<String>,
    val friends: List<String>
)