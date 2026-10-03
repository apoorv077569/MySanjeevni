package com.mysanjeevni.mysanjeevni.features.auth.domain.model

data class AuthResult(
    val message: String,
    val token: String?,
    val user: AuthUser?,
    val phoneVerificationToken:String?
)