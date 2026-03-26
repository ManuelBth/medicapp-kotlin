package com.betha.medicapp.doctor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.betha.medicapp.doctor.ui.screens.DoctorScreen
import com.betha.medicapp.ui.theme.MedicappTheme

class DoctorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val doctorId = intent.getIntExtra("doctorId", 0)
        val userName = intent.getStringExtra("userName") ?: ""
        
        setContent {
            MedicappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DoctorScreen(
                        doctorId = doctorId,
                        userName = userName,
                        onLogout = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}
