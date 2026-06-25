package com.mysanjeevni.mysanjeevni.data.remote.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("message")
    val message: String?,

    @SerializedName("user")
    val user: User?
)
