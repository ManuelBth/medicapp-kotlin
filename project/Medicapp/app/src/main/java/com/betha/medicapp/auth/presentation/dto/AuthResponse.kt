package com.betha.medicapp.auth.presentation.dto

data class AuthResponse(
    val status: String,
    val isSuccess: Boolean,
    val userName: String? = null,
    val isDoctor: Boolean? = null
)