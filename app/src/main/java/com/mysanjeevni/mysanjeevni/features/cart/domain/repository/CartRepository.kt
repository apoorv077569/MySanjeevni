package com.mysanjeevni.mysanjeevni.features.cart.domain.repository


import com.mysanjeevni.mysanjeevni.features.cart.data.local.dao.CartDao
import com.mysanjeevni.mysanjeevni.features.cart.data.local.entity.CartEntity
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.data.remote.model.cart.UpdateCartRequest
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val api: ApiService,
    private val dao: CartDao
) {

    fun getCart(userId: String): Flow<List<CartEntity>> {
        return dao.getCartItems(userId)
    }

    suspend fun addToCart(item: CartEntity) {
        dao.insert(item)
    }

    suspend fun updateLocal(productId: String, userId: String, qty: Int) {
        dao.updateQuantity(productId, userId, qty)
    }

    suspend fun removeLocal(productId: String, userId: String) {
        dao.deleteItem(productId, userId)
    }

    suspend fun syncCartToServer(userId: String, productId: String, qty: Int) {
        api.updateCart(
            UpdateCartRequest(
                userId = userId,
                productId = productId,
                qty = qty
            )
        )
    }

    suspend fun fetchServerCart(
        userId: String
    ): Response<CartResponse> {

        return api.getCart(userId)
    }

    suspend fun updateQuantity(
        productId: String,
        userId: String,
        qty: Int
    ) {
        dao.updateQuantity(productId, userId, qty)
    }
    suspend fun clearCart(userId: String) {
        dao.clearCart(userId)
    }
}