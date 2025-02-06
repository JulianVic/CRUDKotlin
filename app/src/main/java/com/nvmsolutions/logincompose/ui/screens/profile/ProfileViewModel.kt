package com.nvmsolutions.logincompose.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nvmsolutions.logincompose.data.model.User
import com.nvmsolutions.logincompose.data.network.ApiService
import com.nvmsolutions.logincompose.data.network.RetrofitClient
import com.nvmsolutions.logincompose.data.network.UpdateUserRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val apiService: ApiService = RetrofitClient.apiService

    fun setCurrentUser(user: User) {
        _user.value = user
    }
    fun loadUser(userId: String) {
        viewModelScope.launch {
            try {
                val user = apiService.getUser(userId) // Asegúrate de tener este endpoint en tu API
                _user.value = user
                _errorMessage.value = ""
            } catch (e: Exception) {
                _errorMessage.value = "Error loading user: ${e.localizedMessage}"
            }
        }
    }

    // Función para actualizar el nombre del usuario
    fun updateUserName(userId: String, newName: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val updatedUser = apiService.updateUser(userId, UpdateUserRequest(newName))
                _user.value = _user.value?.copy(name = newName)
                _errorMessage.value = ""
                onComplete(true)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error updating user"
                onComplete(false)
            }
        }
    }

    // Función para eliminar el usuario
    fun deleteUser(userId: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                apiService.deleteUser(userId)
                _user.value = null
                _errorMessage.value = ""
                onComplete(true)
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting user: ${e.localizedMessage}"
                onComplete(false)
            }
        }
    }
}