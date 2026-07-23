package com.mysanjeevni.mysanjeevni.features.auth.data.dto

data class LoginRequestDto(
    val role: String,
    val email: String,
    val password: String
)

data class GoogleLoginRequest(
    val idToken: String,
    val fcmToken: String? = null
)
