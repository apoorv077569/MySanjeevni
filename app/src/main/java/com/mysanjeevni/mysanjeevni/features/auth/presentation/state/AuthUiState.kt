package com.mysanjeevni.mysanjeevni.features.auth.presentation.state


import com.mysanjeevni.mysanjeevni.data.remote.model.notification.GenericResponse
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.AuthResponseDto
import com.mysanjeevni.mysanjeevni.features.auth.domain.model.AuthResult

sealed class AuthUiState {

    data object Idle : AuthUiState()

    data object Loading : AuthUiState()

    data class LoginSuccess(
        val data: AuthResult
    ) : AuthUiState()

    data class GoogleLoginSuccess(
        val data: AuthResponseDto
    ) : AuthUiState()

    data class SignupSuccess(
        val data: AuthResult
    ) : AuthUiState()

    data class OtpSent(
        val data: GenericResponse
    ) : AuthUiState()

    data class SignupOtpSent(
        val data: AuthResult
    ) : AuthUiState()

    data class OtpVerified(
        val data: AuthResult
    ) : AuthUiState()

    data class SignupOtpVerified(
        val data: AuthResult
    ) : AuthUiState()

    data class PasswordResetSuccess(
        val data: GenericResponse
    ) : AuthUiState()

    data class Error(
        val message: String
    ) : AuthUiState()
}