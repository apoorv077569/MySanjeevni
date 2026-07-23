package com.mysanjeevni.mysanjeevni.features.auth.data.dto

data class ResetPasswordRequestDto(
    val phone: String,
    val otp: String,
    val newPassword: String,
    val role:String ="user"
)