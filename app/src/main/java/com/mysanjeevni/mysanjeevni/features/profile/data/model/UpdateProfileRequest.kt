package com.mysanjeevni.mysanjeevni.features.profile.data.model

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("userId") val userId: String?,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("fullAddress") val fullAddress: String,
    @SerializedName("profileImage") val profileImage: String? = null
)
