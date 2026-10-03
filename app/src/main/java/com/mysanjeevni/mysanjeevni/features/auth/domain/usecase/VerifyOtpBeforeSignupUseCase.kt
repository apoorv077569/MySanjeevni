package com.mysanjeevni.mysanjeevni.features.auth.domain.usecase

import com.mysanjeevni.mysanjeevni.features.auth.domain.model.AuthResult
import com.mysanjeevni.mysanjeevni.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyOtpBeforeSignupUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone:String,otp:String,role: String): Result<AuthResult>{
        return repository.verifyOtpBeforeSignup(
            phone = phone,
            otp = otp,
            role = role
        )
    }
}