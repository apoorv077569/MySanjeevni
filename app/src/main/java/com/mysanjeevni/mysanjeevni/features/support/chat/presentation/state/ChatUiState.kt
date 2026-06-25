package com.mysanjeevni.mysanjeevni.features.support.chat.presentation.state

import com.mysanjeevni.mysanjeevni.features.support.chat.domain.model.ChatMessage

data class ChatUiState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val error: String? = null
)