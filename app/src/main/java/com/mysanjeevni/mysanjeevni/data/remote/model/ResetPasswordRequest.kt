package com.mysanjeevni.mysanjeevni.data.remote.model

data class ResetPasswordRequest(
    val phone: String,
    val newPassword: String
)