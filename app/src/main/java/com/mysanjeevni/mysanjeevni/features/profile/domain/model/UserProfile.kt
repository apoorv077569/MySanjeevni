package com.mysanjeevni.mysanjeevni.features.profile.domain.model

data class UserProfile(
    val id: String,
    val fullName: String,
    val phone: String,
    val role: String,
    val email: String,
    val profileImage: String?,
    val address: String,
    val isVerified: Boolean
)