package com.mysanjeevni.mysanjeevni.utils

import android.content.Context
import androidx.core.content.edit

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("mysanjeevni_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_IS_LOGIN = "is_login"
    }

    // ✅ Save Login Data
    fun saveLogin(token: String, userId: String) {
        prefs.edit {
            putString(KEY_TOKEN, token)
            putString(KEY_USER_ID, userId)
            putBoolean(KEY_IS_LOGIN, true)
        }
    }

    // 🔑 Get Token
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    // 👤 Get UserId
    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    // ✅ Check Login
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGIN, false)
    }

    // 🚪 Logout
    fun logout() {
        prefs.edit { clear() }
    }
}