package com.betha.medicapp.auth.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var idNumber by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isDoctor by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
            viewModel.onEvent(AuthEvent.ClearMessage)
            // Solo navegar si no hay error (éxito en registro)
            if (!uiState.isError) {
                onRegisterSuccess()
            }
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
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "MedicApp",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )

            Text(
                text = "Crea tu cuenta",
                fontSize = 16.sp,
                color = White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
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
                        text = "Registrarse",
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
                        value = userName,
                        onValueChange = { userName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        placeholder = { Text("Nombre de Usuario", color = Color.Gray) },
                        singleLine = true,
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

                    TextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        placeholder = { Text("Confirmar Contraseña", color = Color.Gray) },
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isDoctor,
                            onCheckedChange = { isDoctor = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = GreenEssenza,
                                uncheckedColor = White.copy(alpha = 0.7f),
                                checkmarkColor = White
                            )
                        )
                        Text(
                            text = "Eres médico",
                            color = White,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    PrimaryButton(
                        text = "Registrarse",
                        onClick = {
                            val id = idNumber.toIntOrNull()
                            when {
                                id == null -> {
                                    Toast.makeText(context, "Ingrese un ID válido", Toast.LENGTH_SHORT).show()
                                }
                                userName.isBlank() -> {
                                    Toast.makeText(context, "Ingrese un nombre de usuario", Toast.LENGTH_SHORT).show()
                                }
                                password.length < 4 -> {
                                    Toast.makeText(context, "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show()
                                }
                                password != confirmPassword -> {
                                    Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    viewModel.onEvent(AuthEvent.Register(id, userName, isDoctor, password))
                                }
                            }
                        },
                        isLoading = uiState.isLoading,
                        enabled = idNumber.isNotEmpty() && userName.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text(
                    text = "¿Ya tienes cuenta? Inicia sesión",
                    color = White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
