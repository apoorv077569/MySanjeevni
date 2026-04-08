package com.mysanjeevni.mysanjeevni.data.remote.model

data class AuthResponse(
    val message: String,
    val token: String?,
    val user: User?
)


