package com.mysanjeevni.mysanjeevni.data.remote.model

data class RegisterRequest(
    val fullName: String,
    val role: String,
    val email: String,
    val phone: String,
    val address: String,
    val password: String
)
