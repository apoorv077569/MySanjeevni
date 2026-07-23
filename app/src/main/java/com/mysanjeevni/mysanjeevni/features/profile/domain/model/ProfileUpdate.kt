package com.mysanjeevni.mysanjeevni.features.profile.domain.model

data class ProfileUpdate(
    val userId: String,
    val fullName: String,
    val phone: String,
    val fullAddress: String,
    val profileImage: String? = null
)