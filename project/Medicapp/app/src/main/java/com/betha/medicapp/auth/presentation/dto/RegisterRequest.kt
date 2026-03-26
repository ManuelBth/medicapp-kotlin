package com.betha.medicapp.auth.presentation.dto

data class RegisterRequest(
    val idNumber: Int,
    val userName: String,
    val doctor: Boolean,
    val password: String
)