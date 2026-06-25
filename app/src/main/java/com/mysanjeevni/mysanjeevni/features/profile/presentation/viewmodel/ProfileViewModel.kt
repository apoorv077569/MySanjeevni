package com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.data.remote.ApiClient
import com.mysanjeevni.mysanjeevni.features.profile.data.repository.ProfileRepository
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
    private val repository: ProfileRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        getProfile()
    }

    private fun getProfile() {

        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            try {

                val token = sessionManager.getToken()
                val userId = sessionManager.getUserId()

                Log.d("PROFILE_DEBUG", "Token: $token")

                val response = repository.getProfile(userId = userId)
                Log.d("PROFILE_DEBUG", "URL: ${ApiClient.api.getProfile(userId).raw().request.url}")

                // 🔍 CHECK 2: API response code kya hai
                Log.d("PROFILE_DEBUG", "Response Code: ${response.code()}")
                Log.d("PROFILE_DEBUG", "Response Successful: ${response.isSuccessful}")

                if (response.isSuccessful) {

                    val rawJson = response.body().toString()
                    Log.d("PROFILE_DEBUG", "Raw JSON: $rawJson")


                    Log.d("PROFILE_DEBUG", "Response Body: ${response.body()}")
                    Log.d("PROFILE_DEBUG", "User: ${response.body()?.user}")
                    Log.d("PROFILE_DEBUG", "UserId: ${response.body()?.user?._id}")

                    _state.value = _state.value.copy(
                        isLoading = false,
                        user = response.body()?.user
                    )

                } else {

                    // 🔍 CHECK 4: Error response kya hai
                    Log.d("PROFILE_DEBUG", "Error Body: ${response.errorBody()?.string()}")
                    Log.d("PROFILE_DEBUG", "Error Message: ${response.message()}")

                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = response.message()
                    )
                }

            } catch (e: Exception) {

                // 🔍 CHECK 5: Exception kya hai
                Log.d("PROFILE_DEBUG", "Exception: ${e.message}")
                Log.d("PROFILE_DEBUG", "Exception Type: ${e.javaClass.simpleName}")

                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown Error"
                )
            }
        }
    }
    fun logout() {
        sessionManager.logout()
    }
}