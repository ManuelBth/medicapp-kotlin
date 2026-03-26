package com.betha.medicapp.auth.presentation.dto

data class ScheduleRequest(
    val patientId: String,
    val doctorName: String,
    val date: String,
    val time: String
)
