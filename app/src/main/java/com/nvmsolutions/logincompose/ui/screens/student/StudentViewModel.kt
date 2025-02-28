package com.nvmsolutions.logincompose.ui.screens.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Student(
    val id: String,
    val name: String,
    val email: String,
    val course: String
)

class StudentViewModel : ViewModel() {
    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    fun loadStudents() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = ""

                // Simulamos que la lista se está cargando desde una base de datos
                delay(2000)

                // Lista de estudiantes simulada
                val studentsList = listOf(
                    Student("1", "Ana López", "ana.lopez@email.com", "Android Development"),
                    Student("2", "Carlos Pérez", "carlos.perez@email.com", "iOS Development"),
                    Student("3", "María González", "maria.gonzalez@email.com", "Web Development"),
                    Student("4", "José Rodríguez", "jose.rodriguez@email.com", "Cloud Computing")
                )

                _students.value = studentsList
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar estudiantes: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}