package com.mysanjeevni.mysanjeevni.features.auth.data.dto

data class SendOtpRequestDto(
    val phone: String,
    val role : String
)

data class SendOtpBeforeSignupRequestDto(
    val phone: String,
    val fullName:String
)
