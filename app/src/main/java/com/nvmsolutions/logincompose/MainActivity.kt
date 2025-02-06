package com.nvmsolutions.logincompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nvmsolutions.logincompose.ui.screens.login.LoginScreen
import com.nvmsolutions.logincompose.ui.screens.login.LoginViewModel
import com.nvmsolutions.logincompose.ui.screens.profile.EditProfileScreen
import com.nvmsolutions.logincompose.ui.screens.profile.ProfileScreen
import com.nvmsolutions.logincompose.ui.screens.profile.ProfileViewModel
import com.nvmsolutions.logincompose.ui.screens.register.RegisterScreen
import com.nvmsolutions.logincompose.ui.screens.register.RegisterViewModel
import com.nvmsolutions.logincompose.ui.theme.LoginComposeTheme

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginComposeTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        val errorMessage by loginViewModel.errorMessage.collectAsState()

                        LoginScreen(
                            onLoginClick = { email, password ->
                                loginViewModel.login(email, password) { success ->
                                    if (success && loginViewModel.user.value != null) {
                                        navController.navigate("profile")
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
                                        navController.navigate("profile")
                                    }
                                }
                            },
                            onLoginClick = {
                                navController.navigate("login")
                            },
                            errorMessage = errorMessage
                        )
                    }

                    composable("profile") {
                        val user by loginViewModel.user.collectAsState()

                        user?.let {
                            ProfileScreen(
                                user = it,
                                onEditProfileClick = {
                                    navController.navigate("editProfile/${it.id}")
                                },
                                onDeleteProfileClick = {
                                    profileViewModel.deleteUser(it.id) { success ->
                                        if (success) {
                                            navController.popBackStack("login", inclusive = false)
                                        }
                                    }
                                }
                            )
                        }
                    }
                    // In the NavHost configuration, add:
                    composable("editProfile/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId") ?: ""
                        val profileViewModel: ProfileViewModel by viewModels()

                        val user by profileViewModel.user.collectAsState()
                        val errorMessage by profileViewModel.errorMessage.collectAsState()

                        user?.let {
                            EditProfileScreen(
                                userId = userId,
                                currentName = it.name,
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
                    }
                }
            }
        }
    }
}