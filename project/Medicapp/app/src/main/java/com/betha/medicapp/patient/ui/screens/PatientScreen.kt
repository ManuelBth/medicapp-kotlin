package com.betha.medicapp.patient.ui.screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.betha.medicapp.patient.presentation.viewmodel.PatientEvent
import com.betha.medicapp.patient.presentation.viewmodel.PatientViewModel
import com.betha.medicapp.auth.ui.components.PrimaryButton
import com.betha.medicapp.ui.theme.*
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

    LaunchedEffect(patientId) {
        viewModel.onEvent(PatientEvent.LoadDoctors(patientId))
    }

    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
            viewModel.onEvent(PatientEvent.ClearMessage)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header azul
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(colors = listOf(PrimaryDark, Primary)),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Área del Paciente", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = OnPrimary)
                    Text(text = "Bienvenido, $userName", fontSize = 14.sp, color = OnPrimary.copy(alpha = 0.8f), modifier = Modifier.padding(top = 4.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card blanco
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Surface, shape = RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text(text = "Agendar Cita", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = OnSurface, modifier = Modifier.padding(bottom = 16.dp))

                    // Selector doctor
                    Text(text = "Seleccionar Doctor", color = TextSecondary, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().background(SurfaceVariant, RoundedCornerShape(12.dp)).clickable { expanded = true }.padding(16.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = uiState.selectedDoctor ?: "Seleccionar doctor", color = if (uiState.selectedDoctor != null) OnSurface else TextSecondary)
                            Text("▼", color = Primary)
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            uiState.doctors.forEach { doctor ->
                                DropdownMenuItem(text = { Text(doctor.doctorName) }, onClick = {
                                    viewModel.onEvent(PatientEvent.SelectDoctor(doctor.doctorName))
                                    expanded = false
                                })
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Fecha
                    Text(text = "Fecha", color = TextSecondary, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().background(SurfaceVariant, RoundedCornerShape(12.dp)).clickable {
                            val cal = Calendar.getInstance()
                            DatePickerDialog(context, { _, year, month, day ->
                                val date = String.format("%02d/%02d/%d", day, month + 1, year)
                                viewModel.onEvent(PatientEvent.SelectDate(date))
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                        }.padding(16.dp)
                    ) {
                        Text(text = uiState.selectedDate ?: "Seleccionar fecha", color = if (uiState.selectedDate != null) OnSurface else TextSecondary)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Hora
                    Text(text = "Hora", color = TextSecondary, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().background(SurfaceVariant, RoundedCornerShape(12.dp)).clickable {
                            val cal = Calendar.getInstance()
                            TimePickerDialog(context, { _, hour, minute ->
                                val time = String.format("%02d:%02d", hour, minute)
                                viewModel.onEvent(PatientEvent.SelectTime(time))
                            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                        }.padding(16.dp)
                    ) {
                        Text(text = uiState.selectedTime ?: "Seleccionar hora", color = if (uiState.selectedTime != null) OnSurface else TextSecondary)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    PrimaryButton(
                        text = "Agendar Cita",
                        onClick = { viewModel.onEvent(PatientEvent.ScheduleAppointment(patientId.toString())) },
                        isLoading = uiState.isLoading,
                        enabled = uiState.selectedDoctor != null && uiState.selectedDate != null && uiState.selectedTime != null
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(onClick = onLogout) {
                Text(text = "Cerrar Sesión", color = Primary, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
