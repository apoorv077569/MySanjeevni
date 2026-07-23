package com.mysanjeevni.mysanjeevni.features.profile.domain.usecase

import com.mysanjeevni.mysanjeevni.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileImageUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        imageBytes: ByteArray,
        fileName:String,
        mimeType:String
    ): Result<String>{
        return repository.uploadProfileImage(
            imageBytes = imageBytes,
            fileName = fileName,
            mimeType = mimeType
        )
    }
}