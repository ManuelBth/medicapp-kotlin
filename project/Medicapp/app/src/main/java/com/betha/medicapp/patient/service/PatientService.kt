package com.betha.medicapp.patient.service

import com.betha.medicapp.patient.presentation.dto.Doctor
import com.betha.medicapp.patient.presentation.dto.ScheduleRequest
import com.betha.medicapp.common.network.RESTClient
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PatientService(private val client: RESTClient) {

    private val gson = Gson()

    suspend fun getDoctors(idNumber: Int): Result<List<Doctor>> = withContext(Dispatchers.IO) {
        try {
            client.httpGetAsync("/getdoctors?idNumber=$idNumber")
            val response = client.wait()

            val jsonArray = gson.fromJson(response, JsonArray::class.java)
            val doctors = mutableListOf<Doctor>()

            for (i in 0 until jsonArray.size()) {
                val json = jsonArray.get(i).asJsonObject
                val doctorName = json.get("doctorName")?.asString ?: ""
                doctors.add(Doctor(doctorName))
            }

            Result.success(doctors)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun scheduleAppointment(request: ScheduleRequest): Result<String> = withContext(Dispatchers.IO) {
        try {
            val body = JsonObject().apply {
                addProperty("patientId", request.patientId)
                addProperty("doctorName", request.doctorName)
                addProperty("date", request.date)
                addProperty("time", request.time)
            }

            client.httpPostAsync("/scheduleappointment", body.toString())
            val response = client.wait()

            val json = gson.fromJson(response, JsonObject::class.java)
            val status = json.get("status")?.asString ?: ""

            Result.success(status)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
