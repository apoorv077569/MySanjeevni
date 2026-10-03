package com.mysanjeevni.mysanjeevni.features.profile.data.dto

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponseDto(

    @SerializedName("message")
    val message: String?,

    @SerializedName("user")
    val user: UserDto?
)