package com.betha.medicapp.auth.presentation.dto

data class LoginRequest(
    val idNumber: Int,
    val password: String
)