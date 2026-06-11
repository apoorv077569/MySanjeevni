package com.mysanjeevni.mysanjeevni.data.remote.model

data class SendNotificationRequest(
    val userId: String,
    val title: String,
    val body: String
)
