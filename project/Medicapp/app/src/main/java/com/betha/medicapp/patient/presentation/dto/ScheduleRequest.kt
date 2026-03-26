package com.betha.medicapp.patient.presentation.dto

data class ScheduleRequest(
    val patientId: String,
    val doctorName: String,
    val date: String,
    val time: String
)
