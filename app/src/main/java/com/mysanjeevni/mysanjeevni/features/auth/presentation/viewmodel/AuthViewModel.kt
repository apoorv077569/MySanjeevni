package com.mysanjeevni.mysanjeevni.features.auth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.data.remote.model.AuthResponse
import com.mysanjeevni.mysanjeevni.features.auth.data.repository.AuthRepository
import com.mysanjeevni.mysanjeevni.features.auth.presentation.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _otpSent = MutableStateFlow(false)
    val otpSent: StateFlow<Boolean> = _otpSent.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    private val _successMessage = MutableStateFlow<String?>(null)
    private val _googleLoginResponse = MutableStateFlow<AuthResponse?>(null)

    val googleLoginResponse = _googleLoginResponse.asStateFlow()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)

    val uiState = _uiState.asStateFlow()

    fun sendOtp(phone: String, role: String = "user") {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val response = repository.sendOtp(phone, role)
                if (response.isSuccessful) {
                    _otpSent.value = true
                    _uiState.value =
                        AuthUiState.OtpSent(response.body()!!)
                } else {
                    _uiState.value = AuthUiState.Error(response.errorBody()?.string() ?: "Failed to send OTP")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Something went wrong")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sendOtpBeforeSignup(phone: String, fullName: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val response = repository.sendOtpBeforeSignup(phone,fullName)
                if (response.isSuccessful) {
                    _otpSent.value = true
                    _uiState.value =
                        AuthUiState.SignupOtpSent(response.body()!!)
                } else {
                    _uiState.value = AuthUiState.Error(response.errorBody()?.string() ?: "Failed to send OTP")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Something went wrong")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun verifyOtpBeforeSignup(phone: String, otp: String, role: String = "user") {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val response = repository.verifyOtpBeforeSignup(phone, otp, role)
                if (response.isSuccessful) {
                    _uiState.value = AuthUiState.SignupOtpVerified(response.body()!!)
                } else {
                    _uiState.value = AuthUiState.Error(response.errorBody()?.string() ?: "Failed to verify OTP")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Something went wrong")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun verifyOtp(phone: String, otp: String, role: String = "user") {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val response = repository.verifyOtp(phone, otp, role)
                if (response.isSuccessful) {
                    _uiState.value = AuthUiState.OtpVerified(response.body()!!)
                }
            } catch (e: Exception) {
                _uiState.value =
                    AuthUiState.Error(e.message ?: "Something went wrong")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(role: String = "user", email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val response =
                    repository.login(role, email, password)
                if (response.isSuccessful) {
                    _uiState.value = AuthUiState.LoginSuccess(response.body()!!)
                } else {
                    _uiState.value = AuthUiState.Error(response.errorBody()?.string() ?: "Login Failed")
                }
            } catch (e: Exception) {
                _uiState.value =
                    AuthUiState.Error(
                        e.message
                            ?: "Something went wrong"
                    )
            }
        }
    }

    fun googleLogin(idToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.googleLogin(idToken)
                if (response.isSuccessful) {
                    _googleLoginResponse.value = response.body()
                    _uiState.value = AuthUiState.GoogleLoginSuccess(response.body()!!)
                } else {
                    _uiState.value = AuthUiState.Error(response.errorBody()?.string() ?: "Google Login Failed")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Something went wrong")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(
        fullName: String,
        role: String = "user",
        email: String,
        phone: String,
        address: String,
        password: String
    ) {
        Log.e(
            "REGISTER_DEBUG",
            "REGISTER FUNCTION ENTERED"
        )
        viewModelScope.launch {
            Log.d(
                "REGISTER_DEBUG",
                """
            fullName=$fullName
            role=$role
            email=$email
            phone=$phone
            address=$address
            password=$password
            """.trimIndent()
            )
            _uiState.value = AuthUiState.Loading
            try {
                val response = repository.signup(fullName, role, email, phone, address, password)
                if (response.isSuccessful) {
                    _uiState.value = AuthUiState.SignupSuccess(response.body()!!)
                } else {
                    _uiState.value = AuthUiState.Error(response.errorBody()?.string() ?: "Registration Failed")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Something went wrong")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetPassword(phone: String,newPassword: String){
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val response = repository.resetPassword(phone,newPassword)
                if (response.body()!= null && response.isSuccessful){
                    _uiState.value = AuthUiState.PasswordResetSuccess(response.body()!!)
                }
            }catch (e: Exception){
                _uiState.value = AuthUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
    fun clearState() {
        _successMessage.value = null
        _error.value = null
    }
}