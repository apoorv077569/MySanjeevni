package com.mysanjeevni.mysanjeevni.features.profile.domain.usecase

import com.mysanjeevni.mysanjeevni.features.profile.domain.model.ProfileUpdate
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.UserProfile
import com.mysanjeevni.mysanjeevni.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(profileUpdate: ProfileUpdate): Result<UserProfile>{
        return repository.updateProfile(profileUpdate)
    }
}