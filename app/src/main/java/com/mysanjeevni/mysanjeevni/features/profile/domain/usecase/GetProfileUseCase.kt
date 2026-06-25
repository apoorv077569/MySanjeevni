package com.mysanjeevni.mysanjeevni.features.profile.domain.usecase

import com.mysanjeevni.mysanjeevni.features.profile.data.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String) =
        repository.getProfile(userId)
}