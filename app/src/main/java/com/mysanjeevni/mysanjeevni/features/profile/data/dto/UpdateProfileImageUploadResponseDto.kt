package com.mysanjeevni.mysanjeevni.features.profile.data.dto

import com.google.gson.annotations.SerializedName

data class ProfileImageUploadResponseDto(

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("imageUrl")
    val imageUrl: String?,

    @SerializedName("publicId")
    val publicId: String?
)