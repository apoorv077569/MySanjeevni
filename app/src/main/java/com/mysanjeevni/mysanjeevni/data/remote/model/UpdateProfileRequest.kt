package com.mysanjeevni.mysanjeevni.data.remote.model

data class UpdateProfileRequest(
    val fullName: String,
    val email: String,
    val phone: String,
    val address: String
)
