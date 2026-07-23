package com.mysanjeevni.mysanjeevni.features.auth.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.data.remote.model.notification.GenericResponse
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.LoginRequestDto
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.RegisterRequestDto
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.ResetPasswordRequestDto
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.SendOtpBeforeSignupRequestDto
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.SendOtpRequestDto
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.VerifyOtpBeforeSignupDto
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.VerifyOtpRequestDto
import com.mysanjeevni.mysanjeevni.features.auth.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.auth.domain.model.AuthResult
import com.mysanjeevni.mysanjeevni.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,

) : AuthRepository {

    private val TAG = "AUTH_REPO"
    override suspend fun login(
        role: String,
        email: String,
        password: String
    ): Result<AuthResult> {
        return try {

            val response = apiService.loginUser(
                LoginRequestDto(
                    role = role,
                    email = email,
                    password = password
                )
            )

            if (response.isSuccessful) {

                val body = response.body()

                if (body != null) {
                    Result.success(body.toDomain())
                } else {
                    Result.failure(
                        Exception("Login response body is empty")
                    )
                }

            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Login failed"
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        fullName: String,
        role: String,
        email: String,
        phone: String,
        address: String,
        password: String,
        phoneVerificationToken:String
    ): Result<AuthResult> {
        return try {
            Log.d(TAG, "================ REGISTER USER ================")
            Log.d(
                TAG,
                "Request -> fullName=$fullName, role=$role, email=$email, phone=$phone, address=$address, phoneVerificationToken=$phoneVerificationToken"
            )

            val response = apiService.registerUser(
                RegisterRequestDto(
                    fullName = fullName,
                    role = role,
                    email = email,
                    phone = phone,
                    address = address,
                    password = password,
                    phoneVerificationToken = phoneVerificationToken
                )
            )

            Log.d(TAG, "Response Code -> ${response.code()}")
            Log.d(TAG, "Response Successful -> ${response.isSuccessful}")

            if (response.isSuccessful) {
                val body = response.body()

                Log.d(TAG, "Response Body -> $body")

                if (body != null) {
                    Log.d(TAG, "User registration successful.")
                    Result.success(body.toDomain())
                } else {
                    Log.e(TAG, "Response body is NULL")

                    Result.failure(
                        Exception("Registration response body is empty")
                    )
                }
            } else {
                val error = response.errorBody()?.string()

                Log.e(TAG, "User registration failed.")
                Log.e(TAG, "Error Body -> $error")

                Result.failure(
                    Exception(
                        error ?: "Registration failed"
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception while registering user", e)
            Result.failure(e)
        }
    }

    override suspend fun sendOtp(
        phone: String,
        role: String
    ): Result<GenericResponse> {

        return try {

            Log.d(TAG, "================ SEND OTP ================")
            Log.d(TAG, "Request -> phone=$phone, role=$role")

            val response = apiService.sendOtp(
                SendOtpRequestDto(
                    phone = phone,
                    role = role
                )
            )

            Log.d(TAG, "Response Code -> ${response.code()}")
            Log.d(TAG, "Response Successful -> ${response.isSuccessful}")

            if (response.isSuccessful) {

                Log.d(TAG, "Response Body -> ${response.body()}")

                response.body()?.let { body ->

                    Log.d(TAG, "OTP sent successfully.")

                    Result.success(body)

                } ?: run {

                    Log.e(TAG, "Response body is null.")

                    Result.failure(
                        Exception("OTP response body is empty")
                    )
                }

            } else {

                val errorBody = response.errorBody()?.string()

                Log.e(TAG, "Send OTP failed.")
                Log.e(TAG, "Error Body -> $errorBody")

                Result.failure(
                    Exception(
                        errorBody ?: "Failed to send OTP"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(TAG, "Exception while sending OTP", e)

            Result.failure(e)
        }
    }

    override suspend fun sendOtpBeforeSignup(
        phone: String,
        fullName: String
    ): Result<AuthResult> {
        return try {
            Log.d(TAG, "================ SEND OTP BEFORE SIGNUP ================")
            Log.d(TAG, "Request -> phone=$phone, fullName=$fullName")

            val response = apiService.sendOtpBeforeSignup(
                SendOtpBeforeSignupRequestDto(
                    phone = phone,
                    fullName = fullName
                )
            )

            Log.d(TAG, "Response Code -> ${response.code()}")
            Log.d(TAG, "Response Successful -> ${response.isSuccessful}")

            if (response.isSuccessful) {
                val body = response.body()

                Log.d(TAG, "Response Body -> $body")

                if (body != null) {
                    Log.d(TAG, "Signup OTP sent successfully.")
                    Result.success(body.toDomain())
                } else {
                    Log.e(TAG, "Response body is NULL")

                    Result.failure(
                        Exception("Signup OTP response body is empty")
                    )
                }
            } else {
                val error = response.errorBody()?.string()

                Log.e(TAG, "Failed to send signup OTP.")
                Log.e(TAG, "Error Body -> $error")

                Result.failure(
                    Exception(
                        error ?: "Failed to send signup OTP"
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception while sending signup OTP", e)
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(
        phone: String,
        otp: String,
        role: String
    ): Result<AuthResult> {

        return try {

            Log.d(TAG, "================ VERIFY OTP ================")
            Log.d(TAG, "Request -> phone=$phone, role=$role, otp=$otp")

            val response = apiService.verifyOtp(
                VerifyOtpRequestDto(
                    phone = phone,
                    otp = otp,
                    role = role
                )
            )

            Log.d(TAG, "Response Code -> ${response.code()}")
            Log.d(TAG, "Response Successful -> ${response.isSuccessful}")

            if (response.isSuccessful) {

                Log.d(TAG, "Response Body -> ${response.body()}")

                val body = response.body()

                if (body != null) {

                    Log.d(TAG, "OTP verification successful.")

                    Result.success(body.toDomain())

                } else {

                    Log.e(TAG, "Response body is null.")

                    Result.failure(
                        Exception("OTP verification response is empty")
                    )
                }

            } else {

                val errorBody = response.errorBody()?.string()

                Log.e(TAG, "OTP verification failed.")
                Log.e(TAG, "Error Body -> $errorBody")

                Result.failure(
                    Exception(
                        errorBody ?: "OTP verification failed"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(TAG, "Exception while verifying OTP", e)

            Result.failure(e)
        }
    }

    override suspend fun verifyOtpBeforeSignup(
        phone: String,
        otp: String,
        role: String
    ): Result<AuthResult> {
        return try {
            Log.d(TAG, "================ VERIFY OTP BEFORE SIGNUP ================")
            Log.d(TAG, "Request -> phone=$phone, role=$role, otp=$otp")

            val response = apiService.verifyBeforeSignup(
                VerifyOtpBeforeSignupDto(
                    phone = phone,
                    otp = otp,
                    role = role
                )
            )
            Log.d(TAG, "Response Code -> ${response.code()}")
            Log.d(TAG, "Response Successful -> ${response.isSuccessful}")
            if (response.isSuccessful) {
                val body = response.body()

                Log.d(TAG, "Response Body -> $body")
                if (body != null) {
                    Log.d(TAG, "OTP verification successful.")
                    Result.success(body.toDomain())
                } else {
                    Log.e(TAG, "Response body is NULL")
                    Result.failure(
                        Exception("Signup OTP verification response is empty")
                    )
                }
            } else {
                val error = response.errorBody()?.string()
                Log.e(TAG, "OTP verification failed.")
                Log.e(TAG, "Error Body -> $error")
                Result.failure(
                    Exception(
                        error ?: "Signup OTP verification failed"
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception while verifying signup OTP", e)
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(
        phone: String,
        otp:String,
        newPassword: String
    ): Result<GenericResponse> {

        return try {

            Log.d(TAG, "================ RESET PASSWORD ================")
            Log.d(TAG, "Request -> phone=$phone")
            Log.d(TAG, "Request -> otp=$otp")
            Log.d(TAG, "Request -> newPasswordLength=${newPassword.length}")

            val request = ResetPasswordRequestDto(
                phone = phone,
                otp = otp,
                newPassword = newPassword
            )

            Log.d(TAG, "Request Body -> $request")

            val response = apiService.resetPassword(request)

            Log.d(TAG, "Response Code -> ${response.code()}")
            Log.d(TAG, "Response Successful -> ${response.isSuccessful}")

            if (response.isSuccessful) {

                Log.d(TAG, "Response Body -> ${response.body()}")

                response.body()?.let { body ->

                    Log.d(TAG, "Password reset successful.")

                    Result.success(body)

                } ?: run {

                    Log.e(TAG, "Response body is null.")

                    Result.failure(
                        Exception("Reset password response is empty")
                    )
                }

            } else {

                val errorBody = response.errorBody()?.string()

                Log.e(TAG, "Password reset failed.")
                Log.e(TAG, "Error Body -> $errorBody")

                Result.failure(
                    Exception(
                        errorBody ?: "Password reset failed"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(TAG, "Exception while resetting password", e)

            Result.failure(e)
        }
    }
}