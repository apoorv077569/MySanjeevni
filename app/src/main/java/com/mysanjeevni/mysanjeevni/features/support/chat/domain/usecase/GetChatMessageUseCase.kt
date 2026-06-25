package com.mysanjeevni.mysanjeevni.features.support.chat.domain.usecase

import com.mysanjeevni.mysanjeevni.features.support.chat.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    suspend operator fun invoke(
        userId: String
    ) = repository.getMessages(userId)
}