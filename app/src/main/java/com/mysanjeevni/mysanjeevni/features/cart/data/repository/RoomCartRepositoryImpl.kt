package com.mysanjeevni.mysanjeevni.features.cart.data.repository

import com.mysanjeevni.mysanjeevni.features.cart.data.local.CartDao
import com.mysanjeevni.mysanjeevni.features.cart.data.local.entity.CartEntity
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RoomCartRepositoryImpl @Inject constructor(
    private val dao: CartDao
) : CartRepository {


    override suspend fun getCartItems(): List<CartItem> {
       val entities = dao.getAllCartItems().first()
        return entities.map{entity ->
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

    override suspend fun updateQuantity(
        itemId: Int,
        newQty: Int
    ): List<CartItem> {
        val currentItems  = dao.getAllCartItems().first()
        val targetItem = currentItems.find{it.id == itemId}

        if (targetItem != null){
            if (newQty > 0){
                val updatedItem = targetItem.copy(qty = newQty)
                dao.insertCartItem(updatedItem)
            }else{
                dao.deleteCartItem(targetItem)
            }
        }
        return getCartItems()
    }

    override suspend fun addToCart(item: CartItem) {
        val entity = CartEntity(
            item.id,
            item.name,
            item.price,
            item.originalPrice,
            1,
            item.imageUrl
        )
        dao.insertCartItem(entity)
    }


}