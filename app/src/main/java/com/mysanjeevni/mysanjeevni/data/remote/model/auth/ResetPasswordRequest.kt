package com.mysanjeevni.mysanjeevni.data.remote.model.auth

data class ResetPasswordRequest(
    val phone: String,
    val newPassword: String
)