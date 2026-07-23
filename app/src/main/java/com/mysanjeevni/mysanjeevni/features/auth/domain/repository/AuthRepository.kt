package com.mysanjeevni.mysanjeevni.features.auth.domain.repository

import com.mysanjeevni.mysanjeevni.data.remote.model.notification.GenericResponse
import com.mysanjeevni.mysanjeevni.features.auth.domain.model.AuthResult

interface AuthRepository {

    suspend fun login(
        role:String,
        email:String,
        password: String
    ): Result<AuthResult>

    suspend fun register(
        fullName: String,
        role:String,
        email: String,
        phone: String,
        address:String,
        password: String,
        phoneVerificationToken:String
    ): Result<AuthResult>

    suspend fun sendOtp(
        phone:String,
        role: String
    ): Result<GenericResponse>

    suspend fun sendOtpBeforeSignup(
        phone: String,
        fullName: String
    ): Result<AuthResult>

    suspend fun verifyOtp(
        phone: String,
        otp: String,
        role: String
    ): Result<AuthResult>

    suspend fun verifyOtpBeforeSignup(
        phone: String,
        otp: String,
        role: String
    ): Result<AuthResult>

    suspend fun resetPassword(
        phone: String,
        otp: String,
        newPassword: String
    ): Result<GenericResponse>
}