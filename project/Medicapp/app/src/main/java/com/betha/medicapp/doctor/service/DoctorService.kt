package com.betha.medicapp.doctor.service

import com.betha.medicapp.doctor.presentation.dto.Appointment
import com.betha.medicapp.common.network.RESTClient
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DoctorService(private val client: RESTClient) {

    private val gson = Gson()

    suspend fun getAppointments(idNumber: Int): Result<List<Appointment>> = withContext(Dispatchers.IO) {
        try {
            client.httpGetAsync("/getappointments?idNumber=$idNumber")
            val response = client.wait()

            val jsonArray = gson.fromJson(response, JsonArray::class.java)
            val appointments = mutableListOf<Appointment>()

            for (i in 0 until jsonArray.size()) {
                val json = jsonArray.get(i).asJsonObject
                val patientName = json.get("patientName")?.asString ?: ""
                val date = json.get("date")?.asString ?: ""
                val time = json.get("time")?.asString ?: ""
                appointments.add(Appointment(patientName, date, time))
            }

            Result.success(appointments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
