package com.mysanjeevni.mysanjeevni.data.remote.model.notification


data class NotificationDto(
    val _id: String,
    val userId: String,
    val type: String,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val actionUrl: String?,
    val createdAt: String,
    val updatedAt: String
)
