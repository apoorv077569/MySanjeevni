package com.mysanjeevni.mysanjeevni.data.remote.model.notification


data class NotificationResponse(
    val message: String,
    val notifications: List<NotificationDto>,
    val unreadCount: Int,
    val total: Int
)