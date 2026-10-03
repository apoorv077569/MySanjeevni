package com.mysanjeevni.mysanjeevni.features.auth.domain.repository
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.AuthResponseDto

interface GoogleAuthRepository {
    suspend fun loginWithGoogle(
        googleIdToken:String
    ):Result<AuthResponseDto>
}