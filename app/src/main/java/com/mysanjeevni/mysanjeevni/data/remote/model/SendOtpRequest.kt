package com.mysanjeevni.mysanjeevni.data.remote.model

data class SendOtpRequest(
    val phone: String,
    val role : String
)
data class SendOtpBeforeSignupRequest(
    val phone: String,
    val fullName:String
)
data class VerifyOtpRequest(
    val phone: String,
    val otp: String,
    val role : String
)