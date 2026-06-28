package com.mysanjeevni.mysanjeevni.features.auth.data.dto

data class ResetPasswordRequest(
    val phone: String,
    val newPassword: String
)