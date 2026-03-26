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
import com.betha.medicapp.ui.theme.*

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
            if (!uiState.isError) {
                onRegisterSuccess()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Header con gradiente azul
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(PrimaryDark, Primary)
                        ),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "MedicApp",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnPrimary
                    )
                    Text(
                        text = "Crea tu cuenta",
                        fontSize = 14.sp,
                        color = OnPrimary.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Card blanco
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Surface,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Registrarse",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurface,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    TextField(
                        value = idNumber,
                        onValueChange = { idNumber = it.filter { c -> c.isDigit() } },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        placeholder = { Text("Número de Identificación", color = TextSecondary) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Primary,
                            unfocusedIndicatorColor = TextSecondary,
                            cursorColor = Primary,
                            focusedTextColor = OnSurface,
                            unfocusedTextColor = OnSurface,
                            focusedContainerColor = SurfaceVariant,
                            unfocusedContainerColor = SurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    TextField(
                        value = userName,
                        onValueChange = { userName = it },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        placeholder = { Text("Nombre de Usuario", color = TextSecondary) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Primary,
                            unfocusedIndicatorColor = TextSecondary,
                            cursorColor = Primary,
                            focusedTextColor = OnSurface,
                            unfocusedTextColor = OnSurface,
                            focusedContainerColor = SurfaceVariant,
                            unfocusedContainerColor = SurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        placeholder = { Text("Contraseña", color = TextSecondary) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Primary,
                            unfocusedIndicatorColor = TextSecondary,
                            cursorColor = Primary,
                            focusedTextColor = OnSurface,
                            unfocusedTextColor = OnSurface,
                            focusedContainerColor = SurfaceVariant,
                            unfocusedContainerColor = SurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    TextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        placeholder = { Text("Confirmar Contraseña", color = TextSecondary) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Primary,
                            unfocusedIndicatorColor = TextSecondary,
                            cursorColor = Primary,
                            focusedTextColor = OnSurface,
                            unfocusedTextColor = OnSurface,
                            focusedContainerColor = SurfaceVariant,
                            unfocusedContainerColor = SurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isDoctor,
                            onCheckedChange = { isDoctor = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Primary,
                                uncheckedColor = TextSecondary,
                                checkmarkColor = OnPrimary
                            )
                        )
                        Text(text = "Eres médico", color = OnSurface, fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    PrimaryButton(
                        text = "Registrarse",
                        onClick = {
                            val id = idNumber.toIntOrNull()
                            when {
                                id == null -> Toast.makeText(context, "Ingrese un ID válido", Toast.LENGTH_SHORT).show()
                                userName.isBlank() -> Toast.makeText(context, "Ingrese un nombre de usuario", Toast.LENGTH_SHORT).show()
                                password.length < 4 -> Toast.makeText(context, "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show()
                                password != confirmPassword -> Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                                else -> viewModel.onEvent(AuthEvent.Register(id, userName, isDoctor, password))
                            }
                        },
                        isLoading = uiState.isLoading,
                        enabled = idNumber.isNotEmpty() && userName.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text(text = "¿Ya tienes cuenta? Inicia sesión", color = Primary, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
