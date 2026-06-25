package com.mysanjeevni.mysanjeevni.features.support.chat.data.dto

data class ChatMessageDto(
    val _id: String?,
    val userId: String,
    val sender: String,
    val message: String,
    val channel: String?,
    val createdAt: String?
)