package com.mysanjeevni.mysanjeevni.features.support.chat.domain.usecase

import com.mysanjeevni.mysanjeevni.features.support.chat.domain.repository.ChatRepository
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    suspend operator fun invoke(
        userId: String,
        userName: String,
        message: String
    ) = repository.sendMessage(
        userId,
        userName,
        message
    )
}