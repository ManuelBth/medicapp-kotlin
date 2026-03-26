package com.betha.medicapp.ui.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.betha.medicapp.auth.presentation.viewmodel.AuthEvent
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
    var loggedInIdNumber by remember { mutableStateOf<Int?>(null) }
    
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Mostrar Toast cuando hay mensaje del servidor
    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
            viewModel.onEvent(AuthEvent.ClearMessage)
        }
    }

    when (val screen = currentScreen) {
        is Screen.Login -> {
            LoginScreen(
                onNavigateToRegister = { currentScreen = Screen.Register },
                onLoginSuccess = { userName, isDoctor, idNumber ->
                    loggedInUser = Pair(userName, isDoctor)
                    loggedInIdNumber = idNumber
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
                        // Llamar al ViewModel para logout en el servidor
                        loggedInIdNumber?.let { idNumber ->
                            viewModel.onEvent(AuthEvent.Logout(idNumber))
                        }
                        // Limpiar estado local inmediatamente (el Toast se mostrará arriba)
                        loggedInUser = null
                        loggedInIdNumber = null
                        currentScreen = Screen.Login
                    },
                    onUserIdChange = { id -> loggedInIdNumber = id }
                )
            }
        }
    }
}
