package com.betha.medicapp.ui.navigation

import android.content.Intent
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
import com.betha.medicapp.auth.ui.screens.login.LoginScreen
import com.betha.medicapp.auth.ui.screens.register.RegisterScreen
import com.betha.medicapp.doctor.DoctorActivity
import com.betha.medicapp.patient.PatientActivity

sealed class Screen {
    object Login : Screen()
    object Register : Screen()
}

@Composable
fun AppNavigation(viewModel: AuthViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
    
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Manejar navegación tras login exitoso - PRIMERO (antes de ClearMessage)
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn && uiState.userName != null) {
            // Guardar datos antes de limpiar
            val isDoctor = uiState.doctor == true
            val idNumber = uiState.idNumber
            val userName = uiState.userName
            
            // Limpiar estado PRIMERO
            viewModel.onEvent(AuthEvent.ClearMessage)
            
            // Luego navegar
            val intent = if (isDoctor) {
                Intent(context, DoctorActivity::class.java).apply {
                    putExtra("doctorId", idNumber)
                    putExtra("userName", userName)
                }
            } else {
                Intent(context, PatientActivity::class.java).apply {
                    putExtra("patientId", idNumber)
                    putExtra("userName", userName)
                }
            }
            context.startActivity(intent)
        }
    }

    // Mostrar Toast cuando hay mensaje del servidor - DESPUÉS
    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
            viewModel.onEvent(AuthEvent.ClearMessage)
        }
    }

    when (currentScreen) {
        is Screen.Login -> {
            LoginScreen(
                onNavigateToRegister = { currentScreen = Screen.Register },
                onLoginSuccess = { 
                    // La navegación se maneja en el LaunchedEffect de arriba
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
    }
}
