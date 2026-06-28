package com.mysanjeevni.mysanjeevni.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mysanjeevni.mysanjeevni.features.cart.data.local.dao.CartDao
import com.mysanjeevni.mysanjeevni.data.local.dao.NotificationDao
import com.mysanjeevni.mysanjeevni.features.cart.data.local.entity.CartEntity
import com.mysanjeevni.mysanjeevni.data.local.entity.NotificationEntity

@Database(
    entities = [
        CartEntity::class,
        NotificationEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao

    abstract fun notificationDao(): NotificationDao
}