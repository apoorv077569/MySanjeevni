package com.mysanjeevni.mysanjeevni.features.auth.data.dto

import com.google.gson.annotations.SerializedName
import com.mysanjeevni.mysanjeevni.data.remote.model.user.User

data class AuthResponseDto(
    @SerializedName("message")
    val message: String?,

    @SerializedName("token")
    val token: String?,

    @SerializedName("phoneVerificationToken")
    val phoneVerificationToken: String?,

    @SerializedName("resetPasswordToken")
    val resetPasswordToken: String?,


    @SerializedName("user")
    val user: User?
)


