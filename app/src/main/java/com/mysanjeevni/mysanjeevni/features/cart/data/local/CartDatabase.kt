package com.mysanjeevni.mysanjeevni.features.cart.data.local

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.mysanjeevni.mysanjeevni.features.cart.data.local.entity.CartEntity

@Database(entities = [CartEntity::class], version = 1)
abstract class CartDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao
}