package com.mysanjeevni.mysanjeevni.features.auth.data.repository

import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.data.remote.api.AuthApiService
import com.mysanjeevni.mysanjeevni.data.remote.model.auth.GoogleLoginRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.auth.LoginRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.auth.RegisterRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.auth.ResetPasswordRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.auth.SendOtpBeforeSignupRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.auth.SendOtpRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.auth.VerifyOtpRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: ApiService,
    private val authApi: AuthApiService
) {

    suspend fun sendOtp(phone: String, role: String) =
        api.sendOtp(SendOtpRequest(phone, role))


    suspend fun sendOtpBeforeSignup(phone: String, fullName: String) =
        api.sendOtpBeforeSignup(SendOtpBeforeSignupRequest(phone, fullName))
    suspend fun verifyOtpBeforeSignup(phone: String, otp: String, role: String) =
        api.verifyBeforeSignup(VerifyOtpRequest(phone, otp, role))


    suspend fun login(role: String, email: String, password: String) =
        api.loginUser(LoginRequest(role, email, password))

    suspend fun googleLogin(
        idToken: String
    ) = authApi.googleSignin(
        GoogleLoginRequest(idToken)
    )

    suspend fun signup(
        fullName: String,
        role: String,
        email: String,
        phone: String,
        address: String,
        password: String
    ) =
        api.registerUser(RegisterRequest(fullName, role, email, phone, address, password))

    suspend fun verifyOtp(phone: String, otp: String, role: String) =
        api.verifyOtp(VerifyOtpRequest(phone, otp, role))

    suspend fun resetPassword( phone: String,newPassword: String) = api.resetPassword(ResetPasswordRequest
        (phone,newPassword))

}



