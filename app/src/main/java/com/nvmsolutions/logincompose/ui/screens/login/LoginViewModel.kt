package com.nvmsolutions.logincompose.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nvmsolutions.logincompose.data.model.LoginRequest
import com.nvmsolutions.logincompose.data.model.User
import com.nvmsolutions.logincompose.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    fun login(email: String, password: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(email, password))
                _user.value = response.user
                _errorMessage.value = ""
                onComplete(true)
            } catch (e: Exception) {
                val errorMsg = when (e) {
                    is retrofit2.HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        "Login failed: ${errorBody ?: e.message() ?: "Unknown error"}"
                    }
                    else -> "Network error: ${e.localizedMessage}"
                }
                println("Login error: $errorMsg")
                _user.value = null
                _errorMessage.value = errorMsg
                onComplete(false)
            }
        }
    }
}
