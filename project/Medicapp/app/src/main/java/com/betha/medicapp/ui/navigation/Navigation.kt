package com.betha.medicapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.betha.medicapp.auth.presentation.viewmodel.AuthViewModel
import com.betha.medicapp.auth.ui.screens.HomeScreen
import com.betha.medicapp.auth.ui.screens.login.LoginScreen
import com.betha.medicapp.auth.ui.screens.register.RegisterScreen

sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object Home : Screen()
}

@Composable
fun AppNavigation(viewModel: AuthViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
    var loggedInUser by remember { mutableStateOf<Pair<String, Boolean>?>(null) }

    when (val screen = currentScreen) {
        is Screen.Login -> {
            LoginScreen(
                onNavigateToRegister = { currentScreen = Screen.Register },
                onLoginSuccess = { userName, isDoctor ->
                    loggedInUser = Pair(userName, isDoctor)
                    currentScreen = Screen.Home
                },
                viewModel = viewModel
            )
        }
        is Screen.Register -> {
            RegisterScreen(
                onNavigateToLogin = { currentScreen = Screen.Login },
                onRegisterSuccess = { currentScreen = Screen.Login },
                viewModel = viewModel
            )
        }
        is Screen.Home -> {
            loggedInUser?.let { userData ->
                HomeScreen(
                    userName = userData.first,
                    isDoctor = userData.second,
                    onLogout = {
                        loggedInUser = null
                        currentScreen = Screen.Login
                    }
                )
            }
        }
    }
}
