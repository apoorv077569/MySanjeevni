package com.mysanjeevni.mysanjeevni.features.auth.domain.usecase

import com.mysanjeevni.mysanjeevni.features.auth.domain.model.AuthResult
import com.mysanjeevni.mysanjeevni.features.auth.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(
        role: String,
        email:String,
        password: String
    ): Result<AuthResult> {
        return repository.login(role,email,password)
    }
}