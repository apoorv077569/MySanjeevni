package com.mysanjeevni.mysanjeevni.features.auth.domain.model

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val role: String,
    val address: String,
    val token: String?
)