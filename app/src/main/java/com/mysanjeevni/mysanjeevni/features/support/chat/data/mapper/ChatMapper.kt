package com.mysanjeevni.mysanjeevni.features.support.chat.data.mapper


import com.mysanjeevni.mysanjeevni.features.support.chat.data.dto.ChatMessageDto
import com.mysanjeevni.mysanjeevni.features.support.chat.domain.model.ChatMessage

fun ChatMessageDto.toDomain() = ChatMessage(
    id = _id ?: "",
    userId = userId,
    sender = sender,
    message = message,
    createdAt = createdAt ?: ""
)