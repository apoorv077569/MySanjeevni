package com.mysanjeevni.mysanjeevni.features.auth.domain.usecase

import com.mysanjeevni.mysanjeevni.features.auth.domain.model.AuthResult
import com.mysanjeevni.mysanjeevni.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(
        fullName: String,
        role: String,
        email:String,
        phone:String,
        address:String,
        password: String,
        phoneVerificationToken:String

        ): Result<AuthResult>{
        return repository.register(
            fullName = fullName,
            role = role,
            email = email,
            phone = phone,
            address = address,
            password = password,
            phoneVerificationToken = phoneVerificationToken
        )
    }
}