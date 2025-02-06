package com.nvmsolutions.logincompose.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nvmsolutions.logincompose.data.model.RegisterRequest
import com.nvmsolutions.logincompose.data.model.User
import com.nvmsolutions.logincompose.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    fun register(name: String, username: String, email: String, password: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            when {
                username.length < 3 -> {
                    _errorMessage.value = "Username must be at least 3 characters long"
                    onComplete(false)
                }
                password.length < 6 -> {
                    _errorMessage.value = "Password must be at least 6 characters long"
                    onComplete(false)
                }
                else -> {
                    try {
                        val response = RetrofitClient.apiService.register(
                            RegisterRequest(name, username, email, password)
                        )
                        _user.value = response.user
                        _errorMessage.value = ""
                        onComplete(true)
                    } catch (e: Exception) {
                        val errorMsg = when (e) {
                            is retrofit2.HttpException -> {
                                val errorBody = e.response()?.errorBody()?.string()
                                "Registration failed: ${errorBody ?: e.message() ?: "Unknown error"}"
                            }
                            else -> "Network error: ${e.localizedMessage}"
                        }
                        println("Register error: $errorMsg")
                        _user.value = null
                        _errorMessage.value = errorMsg
                        onComplete(false)
                    }
                }
            }
        }
    }
}