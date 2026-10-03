package com.mysanjeevni.mysanjeevni.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mysanjeevni.mysanjeevni.features.cart.data.local.dao.CartDao
import com.mysanjeevni.mysanjeevni.data.local.dao.NotificationDao
import com.mysanjeevni.mysanjeevni.features.cart.data.local.entity.CartEntity
import com.mysanjeevni.mysanjeevni.data.local.entity.NotificationEntity

@Database(
    entities = [
        CartEntity::class,
        NotificationEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao

    abstract fun notificationDao(): NotificationDao

    companion object {

        val MIGRATION_4_5 = object : Migration(4, 5) {

            override fun migrate(
                db: SupportSQLiteDatabase
            ) {
                db.execSQL(
                    """
                    ALTER TABLE cart_items
                    ADD COLUMN requirePrescription INTEGER NOT NULL DEFAULT 0
                    """.trimIndent()
                )
            }
        }
    }
}