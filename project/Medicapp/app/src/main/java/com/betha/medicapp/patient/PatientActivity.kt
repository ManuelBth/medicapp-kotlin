package com.betha.medicapp.patient

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.betha.medicapp.MainActivity
import com.betha.medicapp.patient.ui.screens.PatientScreen
import com.betha.medicapp.ui.theme.MedicappTheme

class PatientActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val patientId = intent.getIntExtra("patientId", 0)
        val userName = intent.getStringExtra("userName") ?: ""
        
        setContent {
            MedicappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PatientScreen(
                        patientId = patientId,
                        userName = userName,
                        onLogout = {
                            // Volver a MainActivity con flags para limpiar el stack
                            val intent = Intent(this, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                            finish()
                        }
                    )
                }
            }
        }
    }
}
