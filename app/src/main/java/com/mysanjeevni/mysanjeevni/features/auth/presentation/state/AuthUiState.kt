package com.mysanjeevni.mysanjeevni.features.auth.presentation.state

import com.mysanjeevni.mysanjeevni.data.remote.model.AuthResponse
import com.mysanjeevni.mysanjeevni.data.remote.model.GenericResponse

sealed class AuthUiState {

    object Idle : AuthUiState()

    object Loading : AuthUiState()

    data class LoginSuccess(
        val data: AuthResponse
    ) : AuthUiState()

    data class GoogleLoginSuccess(
        val data: AuthResponse
    ) : AuthUiState()

    data class SignupSuccess(
        val data: AuthResponse
    ) : AuthUiState()

    data class OtpSent(
        val data: GenericResponse
    ) : AuthUiState()

    data class SignupOtpSent(
        val data: AuthResponse
    ) : AuthUiState()

    data class OtpVerified(
        val data: AuthResponse
    ) : AuthUiState()

    data class SignupOtpVerified(
        val data: AuthResponse
    ) : AuthUiState()

    data class PasswordResetSuccess(
        val data: GenericResponse
    ) : AuthUiState()

    data class Error(
        val message: String
    ) : AuthUiState()
}