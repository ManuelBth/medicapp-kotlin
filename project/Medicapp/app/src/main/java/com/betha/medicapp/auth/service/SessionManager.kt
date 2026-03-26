package com.betha.medicapp.auth.service

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "medicapp_session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_IS_DOCTOR = "is_doctor"
    }

    fun saveSession(idNumber: Int, userName: String, isDoctor: Boolean) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, idNumber)
            putString(KEY_USER_NAME, userName)
            putBoolean(KEY_IS_DOCTOR, isDoctor)
            apply()
        }
    }

    fun getIdNumber(): Int = prefs.getInt(KEY_USER_ID, -1)

    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "") ?: ""

    fun isDoctor(): Boolean = prefs.getBoolean(KEY_IS_DOCTOR, false)

    fun isLoggedIn(): Boolean = getIdNumber() != -1

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
