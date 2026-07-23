package com.mysanjeevni.mysanjeevni.features.auth.data.dto

data class VerifyOtpRequestDto(
    val phone: String,
    val otp: String,
    val role: String
)

data class VerifyOtpBeforeSignupDto(
    val phone: String,
    val otp:String,
    val role: String
)
