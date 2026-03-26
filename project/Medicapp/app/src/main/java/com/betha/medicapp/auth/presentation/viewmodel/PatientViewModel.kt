package com.betha.medicapp.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betha.medicapp.auth.presentation.dto.Doctor
import com.betha.medicapp.auth.presentation.dto.ScheduleRequest
import com.betha.medicapp.auth.service.DoctorService
import com.betha.medicapp.auth.service.PatientService
import com.betha.medicapp.common.network.RESTClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PatientUiState(
    val isLoading: Boolean = false,
    val doctors: List<Doctor> = emptyList(),
    val selectedDoctor: String? = null,
    val selectedDate: String? = null,
    val selectedTime: String? = null,
    val message: String? = null,
    val isError: Boolean = false,
    val appointmentScheduled: Boolean = false
)

sealed class PatientEvent {
    data class LoadDoctors(val idNumber: Int) : PatientEvent()
    data class SelectDoctor(val doctorName: String) : PatientEvent()
    data class SelectDate(val date: String) : PatientEvent()
    data class SelectTime(val time: String) : PatientEvent()
    data class ScheduleAppointment(val patientId: String) : PatientEvent()
    object ClearMessage : PatientEvent()
}

class PatientViewModel : ViewModel() {

    private val client = RESTClient("http://192.168.80.22:8080")
    private val patientService = PatientService(client)

    private val _uiState = MutableStateFlow(PatientUiState())
    val uiState: StateFlow<PatientUiState> = _uiState.asStateFlow()

    fun onEvent(event: PatientEvent) {
        when (event) {
            is PatientEvent.LoadDoctors -> loadDoctors(event.idNumber)
            is PatientEvent.SelectDoctor -> selectDoctor(event.doctorName)
            is PatientEvent.SelectDate -> selectDate(event.date)
            is PatientEvent.SelectTime -> selectTime(event.time)
            is PatientEvent.ScheduleAppointment -> scheduleAppointment(event.patientId)
            is PatientEvent.ClearMessage -> clearMessage()
        }
    }

    private fun loadDoctors(idNumber: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null)

            val result = patientService.getDoctors(idNumber)

            result.fold(
                onSuccess = { doctors ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        doctors = doctors,
                        selectedDoctor = doctors.firstOrNull()?.doctorName
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = error.message ?: "Error al cargar doctores",
                        isError = true
                    )
                }
            )
        }
    }

    private fun selectDoctor(doctorName: String) {
        _uiState.value = _uiState.value.copy(selectedDoctor = doctorName)
    }

    private fun selectDate(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }

    private fun selectTime(time: String) {
        _uiState.value = _uiState.value.copy(selectedTime = time)
    }

    private fun scheduleAppointment(patientId: String) {
        val state = _uiState.value

        if (state.selectedDoctor == null || state.selectedDate == null || state.selectedTime == null) {
            _uiState.value = _uiState.value.copy(
                message = "Complete todos los campos",
                isError = true
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null)

            val request = ScheduleRequest(
                patientId = patientId,
                doctorName = state.selectedDoctor,
                date = state.selectedDate,
                time = state.selectedTime
            )

            val result = patientService.scheduleAppointment(request)

            result.fold(
                onSuccess = { status ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = status,
                        isError = false,
                        appointmentScheduled = true
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = error.message ?: "Error al agendar cita",
                        isError = true
                    )
                }
            )
        }
    }

    private fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, isError = false, appointmentScheduled = false)
    }
}
