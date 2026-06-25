package com.mysanjeevni.mysanjeevni.features.support.chat.domain.repository

import com.mysanjeevni.mysanjeevni.features.support.chat.domain.model.ChatMessage

interface ChatRepository {

    suspend fun sendMessage(
        userId: String,
        userName: String,
        message: String
    ): Result<List<ChatMessage>>

    suspend fun getMessages(
        userId: String
    ): Result<List<ChatMessage>>
}