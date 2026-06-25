package com.mysanjeevni.mysanjeevni.features.support.chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.support.chat.domain.usecase.GetChatMessagesUseCase
import com.mysanjeevni.mysanjeevni.features.support.chat.domain.usecase.SendChatMessageUseCase
import com.mysanjeevni.mysanjeevni.features.support.chat.presentation.state.ChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    fun loadMessages(userId: String) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(isLoading = true)
            }

            getChatMessagesUseCase(userId)
                .onSuccess { messages ->

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            messages = messages
                        )
                    }

                }
                .onFailure {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = it.error
                        )
                    }
                }
        }
    }

    fun sendMessage(
        userId: String,
        userName: String,
        message: String
    ) {

        viewModelScope.launch {

            sendChatMessageUseCase(
                userId,
                userName,
                message
            )

            loadMessages(userId)
        }
    }
}