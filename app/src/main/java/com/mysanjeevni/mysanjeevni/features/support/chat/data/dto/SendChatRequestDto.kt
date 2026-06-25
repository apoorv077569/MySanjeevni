package com.mysanjeevni.mysanjeevni.features.support.chat.data.dto

data class SendChatRequestDto(
    val userId: String,
    val userName: String,
    val message: String
)