package com.betha.medicapp.auth.service

import com.betha.medicapp.auth.presentation.dto.AuthResponse
import com.betha.medicapp.auth.presentation.dto.LoginRequest
import com.betha.medicapp.auth.presentation.dto.RegisterRequest
import com.betha.medicapp.common.network.RESTClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthService(private val client: RESTClient) {

    private val gson = Gson()

    suspend fun login(request: LoginRequest): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val body = JsonObject().apply {
                addProperty("idNumber", request.idNumber)
                addProperty("password", request.password)
            }

            client.httpPostAsync("/login", body.toString())
            val response = client.wait()

            val json = gson.fromJson(response, JsonObject::class.java)
            val status = json.get("status")?.asString ?: ""
            
            // El login es exitoso si el status no está vacío
            val isSuccess = status.isNotEmpty()
            
            // Usar el campo "doctor" del JSON directamente
            val doctor = json.get("doctor")?.asBoolean ?: false
            
            // Extraer userName si está disponible, si no usar un valor por defecto
            val userName = json.get("userName")?.asString ?: "Usuario"

            Result.success(AuthResponse(status, isSuccess, userName, doctor))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val body = JsonObject().apply {
                addProperty("idNumber", request.idNumber)
                addProperty("userName", request.userName)
                addProperty("doctor", request.doctor)
                addProperty("password", request.password)
            }

            client.httpPostAsync("/register", body.toString())
            val response = client.wait()

            val json = gson.fromJson(response, JsonObject::class.java)
            val status = json.get("status")?.asString ?: ""
            
            // El registro es exitoso si contiene "registered"
            val isSuccess = status.contains("registered", ignoreCase = true)

            Result.success(AuthResponse(status, isSuccess))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(idNumber: Int): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val body = JsonObject().apply {
                addProperty("idNumber", idNumber)
            }

            client.httpPostAsync("/logout", body.toString())
            val response = client.wait()

            val json = gson.fromJson(response, JsonObject::class.java)
            val status = json.get("status")?.asString ?: ""
            
            // El logout es exitoso si el status no está vacío
            val isSuccess = status.isNotEmpty()

            Result.success(AuthResponse(status, isSuccess))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}