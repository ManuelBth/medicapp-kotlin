package com.betha.medicapp.auth.presentation.dto

data class AuthResponse(
    val message: String,
    val success: Boolean,
    val userName: String? = null,
    val doctor: Boolean? = null
)