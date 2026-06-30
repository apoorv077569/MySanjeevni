package com.mysanjeevni.mysanjeevni.features.auth.data.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val fullName: String,
    val role: String,
    val email: String,
    val phone: String,
    @SerializedName("fullAddress")
    val address: String,
    val password: String
)