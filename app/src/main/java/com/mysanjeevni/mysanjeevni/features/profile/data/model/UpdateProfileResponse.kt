package com.mysanjeevni.mysanjeevni.features.profile.data.model

import com.google.gson.annotations.SerializedName
import com.mysanjeevni.mysanjeevni.data.remote.model.User

data class UpdateProfileResponse(
    @SerializedName("message")
    val message: String?,

    @SerializedName("user")
    val user: User?
)
