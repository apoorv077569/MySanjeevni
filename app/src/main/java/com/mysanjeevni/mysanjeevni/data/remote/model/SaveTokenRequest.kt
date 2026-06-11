package com.mysanjeevni.mysanjeevni.data.remote.model

data class SaveTokenRequest(
    val userId:String,
    val token: String
)

data class GenericResponse(
    val message: String
)
