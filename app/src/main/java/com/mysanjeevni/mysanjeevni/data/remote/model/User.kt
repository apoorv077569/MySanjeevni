package com.mysanjeevni.mysanjeevni.data.remote.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName(value = "_id", alternate = ["id"])
    val _id: String?,

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