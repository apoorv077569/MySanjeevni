package com.mysanjeevni.mysanjeevni.features.auth.domain.usecase

import com.mysanjeevni.mysanjeevni.data.remote.model.notification.GenericResponse
import com.mysanjeevni.mysanjeevni.features.auth.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String,otp:String,newPassword:String): Result<GenericResponse>{
        return repository.resetPassword(
            phone = phone,
            otp = otp,
            newPassword = newPassword
        )
    }
}