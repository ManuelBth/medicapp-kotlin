package com.betha.medicapp.doctor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betha.medicapp.doctor.presentation.dto.Appointment
import com.betha.medicapp.doctor.service.DoctorService
import com.betha.medicapp.common.network.RESTClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DoctorUiState(
    val isLoading: Boolean = false,
    val appointments: List<Appointment> = emptyList(),
    val message: String? = null,
    val isError: Boolean = false
)

sealed class DoctorEvent {
    data class LoadAppointments(val idNumber: Int) : DoctorEvent()
    object ClearMessage : DoctorEvent()
}

class DoctorViewModel : ViewModel() {

    private val client = RESTClient("http://192.168.80.22:8080")
    private val doctorService = DoctorService(client)

    private val _uiState = MutableStateFlow(DoctorUiState())
    val uiState: StateFlow<DoctorUiState> = _uiState.asStateFlow()

    fun onEvent(event: DoctorEvent) {
        when (event) {
            is DoctorEvent.LoadAppointments -> loadAppointments(event.idNumber)
            is DoctorEvent.ClearMessage -> clearMessage()
        }
    }

    private fun loadAppointments(idNumber: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null)

            val result = doctorService.getAppointments(idNumber)

            result.fold(
                onSuccess = { appointments ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        appointments = appointments
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = error.message ?: "Error al cargar citas",
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
