package com.betha.medicapp.doctor

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.betha.medicapp.MainActivity
import com.betha.medicapp.common.preferences.SessionManager
import com.betha.medicapp.doctor.presentation.viewmodel.DoctorViewModel
import com.betha.medicapp.doctor.ui.screens.DoctorScreen
import com.betha.medicapp.ui.theme.MedicappTheme

class DoctorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val sessionManager = SessionManager(this)
        
        val doctorId = intent.getIntExtra("doctorId", 0)
        val userName = intent.getStringExtra("userName") ?: ""
        
        setContent {
            val viewModel: DoctorViewModel = viewModel()
            
            // Inicializar SessionManager
            LaunchedEffect(Unit) {
                viewModel.setSessionManager(sessionManager)
            }
            
            MedicappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DoctorScreen(
                        doctorId = doctorId,
                        userName = userName,
                        onLogout = {
                            // Limpiar sesión antes de navegar
                            viewModel.logout()
                            
                            // Volver a MainActivity con flags para limpiar el stack
                            val intent = Intent(this, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                            finish()
                        },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
