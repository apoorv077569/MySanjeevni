package com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.consult.domnain.usecase.GenerateAgoraTokenUseCase
import com.mysanjeevni.mysanjeevni.features.consult.presentation.state.AgoraCallUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AgoraCallViewModel @Inject constructor(
    private val generateAgoraTokenUseCase: GenerateAgoraTokenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AgoraCallUiState())

    val uiState: StateFlow<AgoraCallUiState> = _uiState.asStateFlow()

    fun generateToken(
        channelName: String,
        participantType: String
    ) {

        Log.d(TAG, "================================")
        Log.d(TAG, "generateToken() called")
        Log.d(TAG, "Channel: $channelName")
        Log.d(TAG, "Participant: $participantType")

        viewModelScope.launch {

            _uiState.value = AgoraCallUiState(
                isLoading = true
            )

            Log.d(TAG, "Calling GenerateAgoraTokenUseCase")

            generateAgoraTokenUseCase(
                channelName = channelName,
                participantType = participantType
            ).onSuccess { response ->

                Log.d(TAG, "Agora token generated successfully")
                Log.d(TAG, "Response: $response")

                _uiState.value = AgoraCallUiState(
                    isLoading = false,
                    tokenData = response
                )

            }.onFailure { exception ->

                Log.e(
                    TAG,
                    "Failed to generate Agora token",
                    exception
                )

                _uiState.value = AgoraCallUiState(
                    isLoading = false,
                    error = exception.message
                        ?: "Failed to generate Agora token"
                )
            }
        }
    }

    fun clearError() {

        _uiState.value = _uiState.value.copy(
            error = null
        )
    }

    companion object {
        private const val TAG = "AgoraCallViewModel"
    }
}