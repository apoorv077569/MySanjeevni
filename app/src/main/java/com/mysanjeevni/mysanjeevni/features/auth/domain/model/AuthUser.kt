package com.mysanjeevni.mysanjeevni.features.auth.domain.model

import com.google.gson.annotations.SerializedName


data class AuthUser(
    @SerializedName("_id")
    val id: String,
    val fullName: String,
    val phone: String,
    val role: String,
    val email: String,
    val profileImage: String?,
    val address: String,
    val isVerified: Boolean
)