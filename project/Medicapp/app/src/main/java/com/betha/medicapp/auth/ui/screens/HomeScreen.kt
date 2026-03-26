package com.betha.medicapp.auth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betha.medicapp.ui.theme.GreenEssenza
import com.betha.medicapp.ui.theme.GreenGradientEnd
import com.betha.medicapp.ui.theme.GreenGradientStart
import com.betha.medicapp.ui.theme.White

@Composable
fun HomeScreen(
    userName: String,
    isDoctor: Boolean,
    onLogout: () -> Unit,
    onUserIdChange: (Int) -> Unit = {}
) {
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡Bienvenido!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = userName,
                fontSize = 24.sp,
                color = White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isDoctor) "Médico" else "Paciente",
                fontSize = 18.sp,
                color = GreenEssenza
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = GreenEssenza
                )
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}
