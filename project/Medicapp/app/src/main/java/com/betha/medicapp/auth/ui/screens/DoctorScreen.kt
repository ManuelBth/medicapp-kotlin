package com.betha.medicapp.auth.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.betha.medicapp.auth.presentation.dto.Appointment
import com.betha.medicapp.auth.presentation.viewmodel.DoctorEvent
import com.betha.medicapp.auth.presentation.viewmodel.DoctorViewModel
import com.betha.medicapp.ui.theme.*

@Composable
fun DoctorScreen(
    doctorId: Int,
    userName: String,
    onLogout: () -> Unit,
    viewModel: DoctorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Cargar citas al iniciar
    LaunchedEffect(doctorId) {
        viewModel.onEvent(DoctorEvent.LoadAppointments(doctorId))
    }

    // Mostrar Toast cuando hay mensaje
    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
            viewModel.onEvent(DoctorEvent.ClearMessage)
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Área del Médico",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )

            Text(
                text = "Dr. $userName",
                fontSize = 16.sp,
                color = White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            Text(
                text = "Citas Pendientes",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (uiState.isLoading) {
                CircularProgressIndicator(color = White)
            } else if (uiState.appointments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay citas programadas",
                        color = White.copy(alpha = 0.7f),
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.appointments) { appointment ->
                        AppointmentCard(appointment = appointment)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = onLogout) {
                Text(
                    text = "Cerrar Sesión",
                    color = White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun AppointmentCard(appointment: Appointment) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = appointment.patientName,
                    color = White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${appointment.date} - ${appointment.time}",
                    color = White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
            Text(
                text = "📅",
                fontSize = 24.sp
            )
        }
    }
}
