package com.mysanjeevni.mysanjeevni.features.cart.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.features.cart.data.local.CartDao
import com.mysanjeevni.mysanjeevni.features.cart.data.local.entity.CartEntity
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomCartRepositoryImpl @Inject constructor(
    private val dao: CartDao
) : CartRepository {


    override fun getCartItems(): Flow<List<CartItem>> {
        return dao.getAllCartItems().map { entities ->
            entities.map { entity ->
                CartItem(
                    entity.id,
                    entity.name,
                    entity.price,
                    entity.originalPrice,
                    entity.qty,
                    entity.imageRes
                )
            }
        }
    }

    override suspend fun updateQuantity(item: Int, newQty: Int): List<CartItem> {
        val currentItems = dao.getAllCartItems().first()
        val targetItem = currentItems.find { it.id == item }
        if (targetItem != null) {
            if (newQty > 0) dao.insertCartItem(targetItem.copy(qty = newQty))
            else dao.deleteCartItem(targetItem)
        }
        return dao.getAllCartItems().first().map {
            CartItem(it.id, it.name, it.price, it.originalPrice, it.qty, it.imageRes)
        }
    }


    override suspend fun addToCart(item: CartItem) {
        Log.d("CART_DEBUG", "Repository addToCart called: ${item.name}")
        val entity = CartEntity(
            item.id, item.name, item.price, item.originalPrice, 1, item.imageUrl
        )
        dao.insertCartItem(entity)
        Log.d("CART_DEBUG", "Inserted into Room: ${entity.name}")
    }


}