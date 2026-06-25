package com.mysanjeevni.mysanjeevni.utils

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences("mysanjeevni_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_IS_LOGIN = "is_login"
        private const val KEY_ROLE = "role"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_USER_ADDRESS = "user_address"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_SELECTED_CITY = "selected_city"

    }

    fun isNotificationEnabled(): Boolean {
        return prefs.getBoolean("notifications", true)
    }
    fun saveSelectedCity(city: String) {
        prefs.edit()
            .putString(KEY_SELECTED_CITY, city)
            .apply()
    }
    fun getSelectedCity(): String {
        return prefs.getString(
            KEY_SELECTED_CITY,
            "India"
        ) ?: "India"
    }
    fun saveUserRole(role: String) {
        prefs.edit {
            putString(KEY_ROLE, role)
        }
    }

    fun getUserRole(): String? {
        return prefs.getString(KEY_ROLE, null)
    }

    fun saveLogin(token: String?, userId: String?) {
        prefs.edit {
            putString(KEY_TOKEN, token)
            putString(KEY_USER_ID, userId)
            putBoolean(KEY_IS_LOGIN, true)
        }
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGIN, false)
    }

    fun logout() {
        prefs.edit { clear() }
    }

    fun saveUserName(userName: String) {
        prefs.edit {
            putString(KEY_USER_NAME, userName)
        }
    }

    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    fun saveUserEmail(userEmail: String){
        prefs.edit {
            putString(KEY_USER_EMAIL,userEmail)
        }
    }
    fun getUserEmail(): String?{
        return prefs.getString(KEY_USER_EMAIL,null)
    }
    fun saveUserAddress(userAddress:String){
        prefs.edit{
            putString(KEY_USER_ADDRESS,userAddress)
        }
    }

    fun getUserAddress():String?{
        return prefs.getString(KEY_USER_ADDRESS,null)
    }

    fun getPhone(): String? {
        return prefs.getString(KEY_USER_PHONE,null)
    }
}