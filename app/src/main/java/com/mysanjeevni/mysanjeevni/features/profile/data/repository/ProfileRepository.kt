package com.mysanjeevni.mysanjeevni.features.profile.data.repository

import com.mysanjeevni.mysanjeevni.data.remote.ApiClient
import com.mysanjeevni.mysanjeevni.features.profile.data.model.UpdateProfileRequest
import javax.inject.Inject

class ProfileRepository @Inject constructor() {
    suspend fun getProfile(userId: String?) =
        ApiClient.api.getProfile(userId)

    suspend fun updateProfile(
        request: UpdateProfileRequest
    )= ApiClient.api.updateProfile(request)
}