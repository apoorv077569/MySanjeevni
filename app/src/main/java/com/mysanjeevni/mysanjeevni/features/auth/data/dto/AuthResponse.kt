package com.mysanjeevni.mysanjeevni.features.auth.data.dto

import com.google.gson.annotations.SerializedName
import com.mysanjeevni.mysanjeevni.data.remote.model.user.User

data class AuthResponse(
    @SerializedName("message")
    val message: String?,

    @SerializedName("token")
    val token: String?,

    @SerializedName("user")
    val user: User?
)