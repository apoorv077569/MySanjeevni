package com.mysanjeevni.mysanjeevni.features.profile.data.dto

import com.google.gson.annotations.SerializedName

data class UserDto(

    @SerializedName(value = "_id", alternate = ["id"])
    val id: String?,

    @SerializedName("fullName")
    val fullName: String?,

    @SerializedName("phone")
    val phone: String?,

    @SerializedName("role")
    val role: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("profileImage")
    val profileImage: String?,

    @SerializedName(value = "address", alternate = ["fullAddress"])
    val address: String?,

    @SerializedName("isVerified")
    val isVerified: Boolean?
)