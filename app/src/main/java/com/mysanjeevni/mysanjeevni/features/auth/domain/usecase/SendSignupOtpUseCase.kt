package com.mysanjeevni.mysanjeevni.features.auth.domain.usecase

import com.mysanjeevni.mysanjeevni.features.auth.domain.model.AuthResult
import com.mysanjeevni.mysanjeevni.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SendSignupOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone:String,fullName:String): Result<AuthResult>{
        return repository.sendOtpBeforeSignup(phone = phone,fullName = fullName)
    }
}