package com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.profile.domain.usecase.GetProfileUseCase
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.ProfileState
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        getProfile()
    }

    fun getProfile() {
        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            val userId = sessionManager.getUserId()?: ""

            Log.d(
                "PROFILE_DEBUG",
                "Fetching profile for userId: $userId"
            )

            if (userId.isBlank()) {
                Log.e(
                    "PROFILE_DEBUG",
                    "User ID is empty"
                )

                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "User ID not found"
                )
                return@launch
            }

            getProfileUseCase(userId)
                .onSuccess { profile ->

                    Log.d(
                        "PROFILE_DEBUG",
                        "Profile fetch successful"
                    )

                    Log.d(
                        "PROFILE_DEBUG",
                        "Profile: $profile"
                    )

                    Log.d(
                        "PROFILE_DEBUG",
                        "User ID: ${profile.id}"
                    )

                    Log.d(
                        "PROFILE_DEBUG",
                        "Full Name: ${profile.fullName}"
                    )

                    Log.d(
                        "PROFILE_DEBUG",
                        "Email: ${profile.email}"
                    )

                    _state.value = _state.value.copy(
                        isLoading = false,
                        user = profile,
                        error = null
                    )
                }
                .onFailure { exception ->

                    Log.e(
                        "PROFILE_DEBUG",
                        "Profile fetch failed",
                        exception
                    )

                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = exception.message
                            ?: "Failed to fetch profile"
                    )
                }
        }
    }

    fun logout() {
        sessionManager.logout()
    }
}