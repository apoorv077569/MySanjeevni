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

    }

    fun isNotificationEnabled(): Boolean {
        return prefs.getBoolean("notifications", true)
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
}