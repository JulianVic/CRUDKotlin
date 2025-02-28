package com.nvmsolutions.logincompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nvmsolutions.logincompose.ui.screens.camera.CameraScreen
import com.nvmsolutions.logincompose.ui.screens.login.LoginScreen
import com.nvmsolutions.logincompose.ui.screens.login.LoginViewModel
import com.nvmsolutions.logincompose.ui.screens.profile.EditProfileScreen
import com.nvmsolutions.logincompose.ui.screens.profile.ProfileScreen
import com.nvmsolutions.logincompose.ui.screens.profile.ProfileViewModel
import com.nvmsolutions.logincompose.ui.screens.register.RegisterScreen
import com.nvmsolutions.logincompose.ui.screens.register.RegisterViewModel
import com.nvmsolutions.logincompose.ui.screens.task.TaskScreen
import com.nvmsolutions.logincompose.ui.screens.task.TaskViewModel
import com.nvmsolutions.logincompose.ui.theme.LoginComposeTheme

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    // Añadimos el TaskViewModel
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginComposeTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login",
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("login") {
                        val errorMessage by loginViewModel.errorMessage.collectAsState()

                        LoginScreen(
                            onLoginClick = { email, password ->
                                loginViewModel.login(email, password) { success ->
                                    if (success && loginViewModel.user.value != null) {
                                        // Cuando el login es exitoso, actualizamos el ProfileViewModel
                                        loginViewModel.user.value?.let { user ->
                                            profileViewModel.setCurrentUser(user)
                                        }
                                        navController.navigate("profile") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                }
                            },
                            onRegisterClick = {
                                navController.navigate("register")
                            },
                            errorMessage = errorMessage
                        )
                    }

                    composable("register") {
                        val errorMessage by registerViewModel.errorMessage.collectAsState()

                        RegisterScreen(
                            onRegisterClick = { name, username, email, password ->
                                registerViewModel.register(name, username, email, password) { success ->
                                    if (success && registerViewModel.user.value != null) {
                                        // Cuando el registro es exitoso, actualizamos el ProfileViewModel
                                        registerViewModel.user.value?.let { user ->
                                            profileViewModel.setCurrentUser(user)
                                        }
                                        navController.navigate("profile") {
                                            popUpTo("register") { inclusive = true }
                                        }
                                    }
                                }
                            },
                            onLoginClick = {
                                navController.popBackStack()
                            },
                            errorMessage = errorMessage
                        )
                    }

                    composable("profile") {
                        val user by profileViewModel.user.collectAsState()

                        user?.let {
                            ProfileScreen(
                                user = it,
                                onEditProfileClick = {
                                    navController.navigate("editProfile/${it.id}")
                                },
                                onDeleteProfileClick = {
                                    profileViewModel.deleteUser(it.id) { success ->
                                        if (success) {
                                            navController.navigate("login") {
                                                popUpTo(0) { inclusive = true }
                                            }
                                        }
                                    }
                                },
                                onOpenCameraClick = {
                                    navController.navigate("camera")
                                },
                                onStudentsClick = {
                                    navController.navigate("tasks")
                                }
                            )
                        }
                    }

                    composable("editProfile/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId") ?: ""

                        val user by profileViewModel.user.collectAsState()
                        val errorMessage by profileViewModel.errorMessage.collectAsState()

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                user != null -> {
                                    EditProfileScreen(
                                        userId = userId,
                                        currentName = user!!.name,
                                        onUpdateClick = { newName ->
                                            profileViewModel.updateUserName(userId, newName) { success ->
                                                if (success) {
                                                    navController.popBackStack()
                                                }
                                            }
                                        },
                                        onCancelClick = {
                                            navController.popBackStack()
                                        },
                                        errorMessage = errorMessage
                                    )
                                }
                                errorMessage.isNotEmpty() -> {
                                    Text(
                                        text = errorMessage,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                                else -> CircularProgressIndicator()
                            }
                        }
                    }

                    composable("camera") {
                        CameraScreen(
                            onPhotoTaken = { file ->
                                Log.d("CameraScreen", "Foto tomada: ${file.absolutePath}")
                                navController.popBackStack()
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // Añadimos la ruta para la nueva pantalla de tareas
                    composable("tasks") {
                        TaskScreen(
                            taskViewModel = taskViewModel,
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}