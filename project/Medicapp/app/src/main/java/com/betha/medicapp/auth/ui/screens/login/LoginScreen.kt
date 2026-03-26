package com.betha.medicapp.auth.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.betha.medicapp.auth.presentation.viewmodel.AuthEvent
import com.betha.medicapp.auth.presentation.viewmodel.AuthViewModel
import com.betha.medicapp.auth.ui.components.PrimaryButton
import com.betha.medicapp.ui.theme.GreenEssenza
import com.betha.medicapp.ui.theme.GreenGradientEnd
import com.betha.medicapp.ui.theme.GreenGradientStart
import com.betha.medicapp.ui.theme.White

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (String, Boolean) -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var idNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
            viewModel.onEvent(AuthEvent.ClearMessage)
        }
    }

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn && uiState.userName != null) {
            onLoginSuccess(uiState.userName!!, uiState.isDoctor ?: false)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GreenGradientStart, GreenGradientEnd)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "MedicApp",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )

            Text(
                text = "Bienvenido de nuevo",
                fontSize = 16.sp,
                color = White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Iniciar Sesión",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = White,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    TextField(
                        value = idNumber,
                        onValueChange = { idNumber = it.filter { c -> c.isDigit() } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        placeholder = { Text("Número de Identificación", color = Color.Gray) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = GreenEssenza,
                            unfocusedIndicatorColor = GreenEssenza.copy(alpha = 0.5f),
                            cursorColor = GreenEssenza,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        placeholder = { Text("Contraseña", color = Color.Gray) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = GreenEssenza,
                            unfocusedIndicatorColor = GreenEssenza.copy(alpha = 0.5f),
                            cursorColor = GreenEssenza,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    PrimaryButton(
                        text = "Iniciar Sesión",
                        onClick = {
                            val id = idNumber.toIntOrNull()
                            if (id != null && password.isNotEmpty()) {
                                viewModel.onEvent(AuthEvent.Login(id, password))
                            } else {
                                Toast.makeText(context, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                            }
                        },
                        isLoading = uiState.isLoading,
                        enabled = idNumber.isNotEmpty() && password.isNotEmpty()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text(
                    text = "¿No tienes cuenta? Regístrate",
                    color = White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
