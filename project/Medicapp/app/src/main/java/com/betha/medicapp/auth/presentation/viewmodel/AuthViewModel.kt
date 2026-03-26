package com.betha.medicapp.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betha.medicapp.auth.presentation.dto.LoginRequest
import com.betha.medicapp.auth.presentation.dto.RegisterRequest
import com.betha.medicapp.auth.service.AuthService
import com.betha.medicapp.common.network.RESTClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userName: String? = null,
    val isDoctor: Boolean? = null,
    val message: String? = null,
    val isError: Boolean = false
)

sealed class AuthEvent {
    data class Login(val idNumber: Int, val password: String) : AuthEvent()
    data class Register(val idNumber: Int, val userName: String, val doctor: Boolean, val password: String) : AuthEvent()
    data class Logout(val idNumber: Int) : AuthEvent()
    object ClearMessage : AuthEvent()
}

class AuthViewModel : ViewModel() {

    private val client = RESTClient("http://192.168.80.22:8080")
    private val authService = AuthService(client)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Login -> login(event.idNumber, event.password)
            is AuthEvent.Register -> register(event.idNumber, event.userName, event.doctor, event.password)
            is AuthEvent.Logout -> logout(event.idNumber)
            is AuthEvent.ClearMessage -> clearMessage()
        }
    }

    private fun login(idNumber: Int, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null)

            val result = authService.login(LoginRequest(idNumber, password))

            result.fold(
                onSuccess = { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = response.isSuccess,
                        userName = response.userName,
                        isDoctor = response.isDoctor,
                        message = response.status,
                        isError = !response.isSuccess
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = error.message ?: "Error de conexión",
                        isError = true
                    )
                }
            )
        }
    }

    private fun register(idNumber: Int, userName: String, doctor: Boolean, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null)

            val result = authService.register(RegisterRequest(idNumber, userName, doctor, password))

            result.fold(
                onSuccess = { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = response.status,
                        isError = !response.isSuccess
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = error.message ?: "Error de conexión",
                        isError = true
                    )
                }
            )
        }
    }

    private fun logout(idNumber: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = authService.logout(idNumber)

            result.fold(
                onSuccess = { response ->
                    _uiState.value = AuthUiState(
                        message = response.status,
                        isError = !response.isSuccess
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = error.message ?: "Error de conexión",
                        isError = true
                    )
                }
            )
        }
    }

    private fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, isError = false)
    }
}