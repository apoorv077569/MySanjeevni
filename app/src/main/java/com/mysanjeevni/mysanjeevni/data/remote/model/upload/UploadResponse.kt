package com.mysanjeevni.mysanjeevni.data.remote.model.upload

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("message") val message: String?,
    @SerializedName("url") val url: String?
)