package com.nvmsolutions.logincompose.data.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Task(
    @SerializedName("_id") val id: String,
    val title: String,
    val description: String,
    val completed: Boolean,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    @SerializedName("__v") val version: Int
) {
    // Función para formatear la fecha de creación en un formato más legible
    fun getFormattedCreatedDate(): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = inputFormat.parse(createdAt)
            date?.let { outputFormat.format(it) } ?: createdAt
        } catch (e: Exception) {
            createdAt
        }
    }
}