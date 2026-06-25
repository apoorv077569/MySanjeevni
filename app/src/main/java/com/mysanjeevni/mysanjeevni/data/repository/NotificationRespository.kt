package com.mysanjeevni.mysanjeevni.data.repository


import com.mysanjeevni.mysanjeevni.data.local.dao.NotificationDao
import com.mysanjeevni.mysanjeevni.data.local.entity.NotificationEntity
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val dao: NotificationDao
) {

    fun getNotifications() =
        dao.getAllNotifications()

    suspend fun insert(notification: NotificationEntity) {
        dao.insert(notification)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }

    suspend fun delete(notification: NotificationEntity) {
        dao.delete(notification)
    }

    suspend fun markAsRead(id: Int) {
        dao.markAsRead(id)
    }
}