package com.mysanjeevni.mysanjeevni.features.cart.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mysanjeevni.mysanjeevni.features.cart.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(item: CartEntity)

    @Query("SELECT * FROM cart_items WHERE userId = :userID")
    fun getCartItems(userID: String): Flow<List<CartEntity>>

    @Query("DELETE FROM cart_items WHERE productId = :productId AND userId = :userID")
    suspend fun deleteItem(productId: String, userID: String)

    @Query("UPDATE cart_items SET quantity = :qty WHERE productId = :productId AND userId = :userId")
    suspend fun updateQuantity(productId: String, userId: String, qty: Int)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: String)



}