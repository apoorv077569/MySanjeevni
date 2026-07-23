//package com.mysanjeevni.mysanjeevni.features.auth.presentation.viewmodel
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import com.mysanjeevni.mysanjeevni.data.remote.model.auth.AuthResponse
//import com.mysanjeevni.mysanjeevni.features.auth.data.repository.AuthRepository
//import com.mysanjeevni.mysanjeevni.features.auth.domain.repository.GoogleAuthRepository
//import com.mysanjeevni.mysanjeevni.features.auth.presentation.state.AuthUiState
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class AuthViewModel @Inject constructor(
//    private val repository: AuthRepository,
//    private val googleAuthRepository: GoogleAuthRepository
//) : ViewModel() {
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//    private val _otpSent = MutableStateFlow(false)
//    val otpSent: StateFlow<Boolean> = _otpSent.asStateFlow()
//    private val _error = MutableStateFlow<String?>(null)
//    val error: StateFlow<String?> = _error.asStateFlow()
//    private val _successMessage = MutableStateFlow<String?>(null)
//    private val _googleLoginResponse = MutableStateFlow<AuthResponse?>(null)
//
//    val googleLoginResponse = _googleLoginResponse.asStateFlow()
//
//    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
//
//    val uiState = _uiState.asStateFlow()
//
//    fun sendOtp(phone: String, role: String = "user") {
//        viewModelScope.launch {
//            _uiState.value = AuthUiState.Loading
//            try {
//                val response = repository.sendOtp(phone, role)
//                if (response.isSuccessful) {
//                    _otpSent.value = true
//                    _uiState.value =
//                        AuthUiState.OtpSent(response.body()!!)
//                } else {
//                    _uiState.value =
//                        AuthUiState.Error(response.errorBody()?.string() ?: "Failed to send OTP")
//                }
//            } catch (e: Exception) {
//                _uiState.value = AuthUiState.Error(e.message ?: "Something went wrong")
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun sendOtpBeforeSignup(phone: String, fullName: String) {
//        viewModelScope.launch {
//            _uiState.value = AuthUiState.Loading
//            try {
//                val response = repository.sendOtpBeforeSignup(phone, fullName)
//                if (response.isSuccessful) {
//                    _otpSent.value = true
//                    _uiState.value =
//                        AuthUiState.SignupOtpSent(response.body()!!)
//                } else {
//                    _uiState.value =
//                        AuthUiState.Error(response.errorBody()?.string() ?: "Failed to send OTP")
//                }
//            } catch (e: Exception) {
//                _uiState.value = AuthUiState.Error(e.message ?: "Something went wrong")
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun verifyOtpBeforeSignup(phone: String, otp: String, role: String = "user") {
//        viewModelScope.launch {
//            _uiState.value = AuthUiState.Loading
//            try {
//                val response = repository.verifyOtpBeforeSignup(phone, otp, role)
//                if (response.isSuccessful) {
//                    _uiState.value = AuthUiState.SignupOtpVerified(response.body()!!)
//                } else {
//                    _uiState.value =
//                        AuthUiState.Error(response.errorBody()?.string() ?: "Failed to verify OTP")
//                }
//            } catch (e: Exception) {
//                _uiState.value = AuthUiState.Error(e.message ?: "Something went wrong")
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun verifyOtp(phone: String, otp: String, role: String = "user") {
//        viewModelScope.launch {
//            _uiState.value = AuthUiState.Loading
//            try {
//                val response = repository.verifyOtp(phone, otp, role)
//                if (response.isSuccessful) {
//                    _uiState.value = AuthUiState.OtpVerified(response.body()!!)
//                }
//            } catch (e: Exception) {
//                _uiState.value =
//                    AuthUiState.Error(e.message ?: "Something went wrong")
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun login(role: String = "user", email: String, password: String) {
//        viewModelScope.launch {
//            _uiState.value = AuthUiState.Loading
//            try {
//                val response =
//                    repository.login(role, email, password)
//                if (response.isSuccessful) {
//                    _uiState.value = AuthUiState.LoginSuccess(response.body()!!)
//                } else {
//                    _uiState.value =
//                        AuthUiState.Error(response.errorBody()?.string() ?: "Login Failed")
//                }
//            } catch (e: Exception) {
//                _uiState.value =
//                    AuthUiState.Error(
//                        e.message
//                            ?: "Something went wrong"
//                    )
//            }
//        }
//    }
//
////    fun googleLogin(idToken: String) {
////        viewModelScope.launch {
////            _isLoading.value = true
////            try {
////                val response = repository.googleLogin(idToken)
////                if (response.isSuccessful) {
////                    _googleLoginResponse.value = response.body()
////                    _uiState.value = AuthUiState.GoogleLoginSuccess(response.body()!!)
////                } else {
////                    _uiState.value = AuthUiState.Error(response.errorBody()?.string() ?: "Google Login Failed")
////                }
////            } catch (e: Exception) {
////                _uiState.value = AuthUiState.Error(e.message ?: "Something went wrong")
////            } finally {
////                _isLoading.value = false
////            }
////        }
////    }
//
//    fun googleLogin(googleIdToken: String) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                val result =
//                    googleAuthRepository.loginWithGoogle(
//                        googleIdToken
//                    )
//                result.onSuccess { response ->
//                    _googleLoginResponse.value = response
//                    _uiState.value =
//                        AuthUiState.GoogleLoginSuccess(response)
//                }
//                    .onFailure { e ->
//                        _uiState.value =
//                            AuthUiState.Error(
//                                e.message ?: "Google Login Failed"
//                            )
//                    }
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun register(
//        fullName: String,
//        role: String = "user",
//        email: String,
//        phone: String,
//        address: String,
//        password: String
//    ) {
//        Log.e(
//            "REGISTER_DEBUG",
//            "REGISTER FUNCTION ENTERED"
//        )
//        viewModelScope.launch {
//            Log.d(
//                "REGISTER_DEBUG",
//                """
//            fullName=$fullName
//            role=$role
//            email=$email
//            phone=$phone
//            address=$address
//            password=$password
//            """.trimIndent()
//            )
//            _uiState.value = AuthUiState.Loading
//            try {
//                val response = repository.signup(fullName, role, email, phone, address, password)
//                if (response.isSuccessful) {
//                    _uiState.value = AuthUiState.SignupSuccess(response.body()!!)
//                } else {
//                    _uiState.value =
//                        AuthUiState.Error(response.errorBody()?.string() ?: "Registration Failed")
//                }
//            } catch (e: Exception) {
//                _uiState.value = AuthUiState.Error(e.message ?: "Something went wrong")
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun resetPassword(phone: String, newPassword: String) {
//        viewModelScope.launch {
//            _uiState.value = AuthUiState.Loading
//            try {
//                val response = repository.resetPassword(phone, newPassword)
//                if (response.body() != null && response.isSuccessful) {
//                    _uiState.value = AuthUiState.PasswordResetSuccess(response.body()!!)
//                }
//            } catch (e: Exception) {
//                _uiState.value = AuthUiState.Error(e.message ?: "Something went wrong")
//            }
//        }
//    }
//
//    fun clearState() {
//        _successMessage.value = null
//        _error.value = null
//    }
//}


package com.mysanjeevni.mysanjeevni.features.auth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.AuthResponseDto
import com.mysanjeevni.mysanjeevni.features.auth.domain.repository.GoogleAuthRepository
import com.mysanjeevni.mysanjeevni.features.auth.domain.usecase.LoginUseCase
import com.mysanjeevni.mysanjeevni.features.auth.domain.usecase.RegisterUserUseCase
import com.mysanjeevni.mysanjeevni.features.auth.domain.usecase.ResetPasswordUseCase
import com.mysanjeevni.mysanjeevni.features.auth.domain.usecase.SendOtpUseCase
import com.mysanjeevni.mysanjeevni.features.auth.domain.usecase.SendSignupOtpUseCase
import com.mysanjeevni.mysanjeevni.features.auth.domain.usecase.VerifyOtpBeforeSignupUseCase
import com.mysanjeevni.mysanjeevni.features.auth.domain.usecase.VerifyOtpUseCase
import com.mysanjeevni.mysanjeevni.features.auth.presentation.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(

    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUserUseCase,
    private val sendOtpUseCase: SendOtpUseCase,
    private val sendSignupOtpUseCase: SendSignupOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val verifySignupOtpUseCase: VerifyOtpBeforeSignupUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val googleAuthRepository: GoogleAuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _otpSent = MutableStateFlow(false)
    val otpSent: StateFlow<Boolean> = _otpSent.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    private var phoneVerificationToken: String? = null


    private val _successMessage = MutableStateFlow<String?>(null)

    // Google response unchanged
    private val _googleLoginResponse =
        MutableStateFlow<AuthResponseDto?>(null)

    val googleLoginResponse =
        _googleLoginResponse.asStateFlow()

    private val _uiState =
        MutableStateFlow<AuthUiState>(
            AuthUiState.Idle
        )

    val uiState = _uiState.asStateFlow()


    // ================================
    // SEND OTP
    // ================================

    fun sendOtp(
        phone: String,
        role: String = "user"
    ) {
        viewModelScope.launch {

            _uiState.value = AuthUiState.Loading
            _isLoading.value = true

            sendOtpUseCase(
                phone = phone,
                role = role
            )
                .onSuccess { response ->

                    _otpSent.value = true

                    _uiState.value =
                        AuthUiState.OtpSent(response)
                }
                .onFailure { error ->

                    _uiState.value =
                        AuthUiState.Error(
                            error.message
                                ?: "Failed to send OTP"
                        )
                }

            _isLoading.value = false
        }
    }


    // ================================
    // SEND SIGNUP OTP
    // ================================

    fun sendOtpBeforeSignup(
        phone: String,
        fullName: String
    ) {
        viewModelScope.launch {

            _uiState.value = AuthUiState.Loading
            _isLoading.value = true

            sendSignupOtpUseCase(
                phone = phone,
                fullName = fullName
            )
                .onSuccess { response ->

                    _otpSent.value = true

                    _uiState.value =
                        AuthUiState.SignupOtpSent(
                            response
                        )
                }
                .onFailure { error ->

                    _uiState.value =
                        AuthUiState.Error(
                            error.message
                                ?: "Failed to send signup OTP"
                        )
                }

            _isLoading.value = false
        }
    }


    // ================================
    // VERIFY SIGNUP OTP
    // ================================

    fun verifyOtpBeforeSignup(
        phone: String,
        otp: String,
        role: String = "user"
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            _isLoading.value = true
            verifySignupOtpUseCase(
                phone = phone,
                otp = otp,
                role = role
            ).onSuccess { response ->

                phoneVerificationToken = response.phoneVerificationToken

                Log.d(
                    "AUTH_VM",
                    "Phone Verification Token = $phoneVerificationToken"
                )

                _uiState.value =
                    AuthUiState.SignupOtpVerified(response)
            }
                .onFailure { error ->
                    _uiState.value =
                        AuthUiState.Error(
                            error.message
                                ?: "Failed to verify signup OTP"
                        )
                }
            _isLoading.value = false
        }
    }


    // ================================
    // VERIFY OTP
    // ================================

    fun verifyOtp(
        phone: String,
        otp: String,
        role: String = "user"
    ) {
        viewModelScope.launch {

            _uiState.value = AuthUiState.Loading
            _isLoading.value = true

            verifyOtpUseCase(
                phone = phone,
                otp = otp,
                role = role
            )
                .onSuccess { response ->

                    _uiState.value =
                        AuthUiState.OtpVerified(
                            response
                        )
                }
                .onFailure { error ->

                    _uiState.value =
                        AuthUiState.Error(
                            error.message
                                ?: "OTP verification failed"
                        )
                }

            _isLoading.value = false
        }
    }


    // ================================
    // LOGIN
    // ================================

    fun login(
        role: String = "user",
        email: String,
        password: String
    ) {
        viewModelScope.launch {

            _uiState.value = AuthUiState.Loading
            _isLoading.value = true

            loginUseCase(
                role = role,
                email = email,
                password = password
            )
                .onSuccess { response ->

                    _uiState.value =
                        AuthUiState.LoginSuccess(
                            response
                        )
                }
                .onFailure { error ->

                    _uiState.value =
                        AuthUiState.Error(
                            error.message
                                ?: "Login Failed"
                        )
                }

            _isLoading.value = false
        }
    }


    // ================================
    // GOOGLE LOGIN
    // UNCHANGED
    // ================================

    fun googleLogin(
        googleIdToken: String
    ) {
        viewModelScope.launch {

            _isLoading.value = true

            try {
                Log.d("GOOGLE_DEBUG", "Firebase Token Length = ${googleIdToken.length}")
                Log.d("GOOGLE_DEBUG", "Firebase Token Prefix = ${googleIdToken.take(30)}...")
                val result =
                    googleAuthRepository.loginWithGoogle(
                        googleIdToken
                    )

                result.onSuccess { response ->

                    _googleLoginResponse.value = response

                    _uiState.value =
                        AuthUiState.GoogleLoginSuccess(
                            response
                        )
                }

                result.onFailure { error ->

                    _uiState.value =
                        AuthUiState.Error(
                            error.message
                                ?: "Google Login Failed"
                        )
                }

            } finally {

                _isLoading.value = false
            }
        }
    }


    // ================================
    // REGISTER
    // ================================

    fun register(
        fullName: String,
        role: String = "user",
        email: String,
        phone: String,
        address: String,
        password: String
        ) {

        Log.d(
            "REGISTER_DEBUG",
            """
            REGISTER REQUEST
            fullName=$fullName
            role=$role
            email=$email
            phone=$phone
            address=$address
            """.trimIndent()
        )

        viewModelScope.launch {

            _uiState.value = AuthUiState.Loading
            _isLoading.value = true

            registerUseCase(
                fullName = fullName,
                role = role,
                email = email,
                phone = phone,
                address = address,
                password = password,
                phoneVerificationToken = phoneVerificationToken
                    ?: throw IllegalStateException("Phone verification token missing")

                )
                .onSuccess { response ->

                    Log.d(
                        "REGISTER_DEBUG",
                        "Registration Success: $response"
                    )

                    _uiState.value =
                        AuthUiState.SignupSuccess(
                            response
                        )
                }
                .onFailure { error ->

                    Log.e(
                        "REGISTER_DEBUG",
                        "Registration Failed",
                        error
                    )

                    _uiState.value =
                        AuthUiState.Error(
                            error.message
                                ?: "Registration Failed"
                        )
                }

            _isLoading.value = false
        }
    }


    // ================================
    // RESET PASSWORD
    // ================================

    fun resetPassword(
        phone: String,
        otp: String,
        newPassword: String
    )
    {
        viewModelScope.launch {

            _uiState.value = AuthUiState.Loading
            _isLoading.value = true

            resetPasswordUseCase(
                phone = phone,
                otp = otp,
                newPassword = newPassword
            )
                .onSuccess { response ->

                    _uiState.value =
                        AuthUiState.PasswordResetSuccess(
                            response
                        )
                }
                .onFailure { error ->

                    _uiState.value =
                        AuthUiState.Error(
                            error.message
                                ?: "Password reset failed"
                        )
                }

            _isLoading.value = false
        }
    }


    // ================================
    // CLEAR STATE
    // ================================

    fun clearState() {
        _successMessage.value = null
        _error.value = null
        _uiState.value = AuthUiState.Idle
    }
}