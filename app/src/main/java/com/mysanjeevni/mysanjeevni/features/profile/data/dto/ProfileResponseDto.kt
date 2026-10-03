package com.mysanjeevni.mysanjeevni.features.profile.data.dto

import com.google.gson.annotations.SerializedName

data class ProfileResponseDto(

    @SerializedName("message")
    val message: String?,

    @SerializedName("user")
    val user: UserDto?
)