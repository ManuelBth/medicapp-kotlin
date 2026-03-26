package com.betha.medicapp.auth.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.betha.medicapp.auth.presentation.viewmodel.PatientEvent
import com.betha.medicapp.auth.presentation.viewmodel.PatientViewModel
import com.betha.medicapp.auth.ui.components.PrimaryButton
import com.betha.medicapp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PatientScreen(
    patientId: Int,
    userName: String,
    onLogout: () -> Unit,
    viewModel: PatientViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    // Cargar doctores al iniciar
    LaunchedEffect(patientId) {
        viewModel.onEvent(PatientEvent.LoadDoctors(patientId))
    }

    // Mostrar Toast cuando hay mensaje
    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
            viewModel.onEvent(PatientEvent.ClearMessage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BlueGradientStart, BlueGradientEnd)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Área del Paciente",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )

            Text(
                text = "Bienvenido, $userName",
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
                        text = "Agendar Cita",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Selector de Doctor
                    Text(
                        text = "Seleccionar Doctor",
                        color = White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .clickable { expanded = true }
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uiState.selectedDoctor ?: "Seleccionar doctor",
                                color = if (uiState.selectedDoctor != null) Color.Black else Color.Gray
                            )
                            Text("▼", color = BlueEssenza)
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            uiState.doctors.forEach { doctor ->
                                DropdownMenuItem(
                                    text = { Text(doctor.doctorName) },
                                    onClick = {
                                        viewModel.onEvent(PatientEvent.SelectDoctor(doctor.doctorName))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Selector de Fecha
                    Text(
                        text = "Fecha",
                        color = White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .clickable {
                                val calendar = Calendar.getInstance()
                                DatePickerDialog(
                                    context,
                                    { _, year, month, day ->
                                        val date = String.format("%02d/%02d/%d", day, month + 1, year)
                                        viewModel.onEvent(PatientEvent.SelectDate(date))
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = uiState.selectedDate ?: "Seleccionar fecha",
                            color = if (uiState.selectedDate != null) Color.Black else Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Selector de Hora
                    Text(
                        text = "Hora",
                        color = White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .clickable {
                                val calendar = Calendar.getInstance()
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        val time = String.format("%02d:%02d", hour, minute)
                                        viewModel.onEvent(PatientEvent.SelectTime(time))
                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    true
                                ).show()
                            }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = uiState.selectedTime ?: "Seleccionar hora",
                            color = if (uiState.selectedTime != null) Color.Black else Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        text = "Agendar Cita",
                        onClick = {
                            viewModel.onEvent(PatientEvent.ScheduleAppointment(patientId.toString()))
                        },
                        isLoading = uiState.isLoading,
                        enabled = uiState.selectedDoctor != null && 
                                 uiState.selectedDate != null && 
                                 uiState.selectedTime != null
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onLogout) {
                Text(
                    text = "Cerrar Sesión",
                    color = White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
