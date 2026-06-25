package com.mysanjeevni.mysanjeevni.features.profile.domain.usecase

import com.mysanjeevni.mysanjeevni.features.profile.data.model.UpdateProfileRequest
import com.mysanjeevni.mysanjeevni.features.profile.data.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        request: UpdateProfileRequest
    ) = repository.updateProfile(request)
}