package com.mysanjeevni.mysanjeevni.features.profile.domain.repository

import com.mysanjeevni.mysanjeevni.features.profile.domain.model.ProfileUpdate
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(
        userId: String
    ): Result<UserProfile>

    suspend fun updateProfile(
        profileUpdate: ProfileUpdate
    ): Result<UserProfile>

    suspend fun uploadProfileImage(
        imageBytes: ByteArray,
        fileName:String,
        mimeType: String
    ): Result<String>
}