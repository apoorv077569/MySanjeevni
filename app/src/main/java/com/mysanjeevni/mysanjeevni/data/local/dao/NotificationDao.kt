package com.mysanjeevni.mysanjeevni.data.local.dao

import androidx.room.*
import com.mysanjeevni.mysanjeevni.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)

    @Query("""
        SELECT * FROM notifications
        ORDER BY timestamp DESC
    """)
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("DELETE FROM notifications")
    suspend fun clearAll()

    @Delete
    suspend fun delete(notification: NotificationEntity)

    @Query("""
        UPDATE notifications
        SET isRead = 1
        WHERE id = :id
    """)
    suspend fun markAsRead(id: Int)

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()
}