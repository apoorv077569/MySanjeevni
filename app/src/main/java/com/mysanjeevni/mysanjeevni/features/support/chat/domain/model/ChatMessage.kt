package com.mysanjeevni.mysanjeevni.features.support.chat.domain.model

data class ChatMessage(
    val id: String,
    val userId: String,
    val sender: String,
    val message: String,
    val createdAt: String
)