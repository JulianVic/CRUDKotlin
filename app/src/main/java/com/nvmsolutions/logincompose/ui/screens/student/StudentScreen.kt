package com.nvmsolutions.logincompose.ui.screens.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StudentScreen(
    studentViewModel: StudentViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val students by studentViewModel.students.collectAsState()
    val isLoading by studentViewModel.isLoading.collectAsState()
    val errorMessage by studentViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBackClick
            ) {
                Text("Atrás")
            }

            Text(
                text = "Lista de Estudiantes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(64.dp)) // Para balancear el layout
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { studentViewModel.loadStudents() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cargar Estudiantes")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Cargando estudiantes...")
                    }
                }
            }
            errorMessage.isNotEmpty() -> {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            students.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No hay estudiantes cargados. Pulse el botón para cargar.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            else -> {
                LazyColumn {
                    items(students) { student ->
                        StudentItem(student)
                    }
                }
            }
        }
    }
}

@Composable
fun StudentItem(student: Student) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = student.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Email: ${student.email}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Curso: ${student.course}")
        }
    }
}