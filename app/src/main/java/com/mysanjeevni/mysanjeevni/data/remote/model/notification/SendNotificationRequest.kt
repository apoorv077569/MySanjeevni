package com.mysanjeevni.mysanjeevni.data.remote.model.notification

data class SendNotificationRequest(
    val userId: String,
    val title: String,
    val body: String
)
