package com.mysanjeevni.mysanjeevni.data.remote.model.auth

class LoginRequest(

    val role:String,
    val email: String,
    val password: String
)

data class GoogleLoginRequest(
    val idToken: String,
    val fcmToken: String? = null
)