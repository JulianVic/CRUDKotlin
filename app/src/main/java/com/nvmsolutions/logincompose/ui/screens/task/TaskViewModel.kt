package com.nvmsolutions.logincompose.ui.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nvmsolutions.logincompose.data.model.Task
import com.nvmsolutions.logincompose.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    fun loadTasks() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = ""

                // Realizamos la llamada a la API para obtener las tareas
                val tasksList = RetrofitClient.apiService.getTasks()
                _tasks.value = tasksList
            } catch (e: Exception) {
                val errorMsg = when (e) {
                    is retrofit2.HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        "Error al cargar tareas: ${errorBody ?: e.message() ?: "Error desconocido"}"
                    }
                    else -> "Error de red: ${e.localizedMessage}"
                }
                _errorMessage.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }
}