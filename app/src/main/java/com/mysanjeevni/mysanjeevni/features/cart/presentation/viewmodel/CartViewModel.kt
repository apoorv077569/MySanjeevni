package com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.data.local.cart.CartEntity
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.data.remote.model.UpdateCartRequest
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.domain.repository.CartRepository
import com.mysanjeevni.mysanjeevni.features.cart.domain.usecase.AddToCartUseCase
import com.mysanjeevni.mysanjeevni.features.cart.domain.usecase.DeleteCartUseCase
import com.mysanjeevni.mysanjeevni.features.cart.domain.usecase.GetCartUseCase
import com.mysanjeevni.mysanjeevni.features.cart.domain.usecase.UpdateCartUseCase
import com.mysanjeevni.mysanjeevni.features.cart.presentation.state.CartState
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val api: ApiService,
    private val repository: CartRepository,
    private val sessionManager: SessionManager,
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state

    init {
        loadCart()
    }
    fun loadCart() {
        viewModelScope.launch {
            val userId = sessionManager.getUserId() ?: return@launch
            getCartUseCase(userId).collect { entities ->
                val items = entities.map {
                    CartItem(
                        id = it.productId,
                        name = it.name,
                        price = it.price,
                        originalPrice = it.originalPrice,
                        qty = it.quantity,
                        imageUrl = it.imageUrl
                    )
                }
                updateState(items)
            }
        }
    }
    fun addToCart(item: CartItem) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId() ?: return@launch
            val entity = CartEntity(
                productId = item.id,
                userId = userId,
                name = item.name,
                price = item.price,
                originalPrice = item.originalPrice,
                quantity = 1,
                imageUrl = item.imageUrl
            )
            repository.addToCart(entity)
            try {
                repository.syncCartToServer(
                    userId = userId,
                    productId = item.id,
                    qty = 1
                )
            } catch (e: Exception) {
                Log.e("CART_SYNC", "Exception = ${e.javaClass.simpleName}")
                Log.e("CART_SYNC", "Message = ${e.message}")
                Log.e("CART_SYNC", Log.getStackTraceString(e))
            }
        }
    }

    fun incrementQty(item: CartItem) {

        viewModelScope.launch {

            val userId =
                sessionManager.getUserId()
                    ?: return@launch

            val qty = item.qty + 1

            repository.updateQuantity(
                item.id,
                userId,
                qty
            )

            try {

                repository.syncCartToServer(
                    userId = userId,
                    productId = item.id,
                    qty = qty
                )

            } catch (e: Exception) {

                Log.e(
                    "CART_SYNC",
                    "Increment Sync Failed ${e.message}"
                )
            }
        }
    }
    fun decrementQty(item: CartItem) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId() ?: return@launch
            val qty = item.qty - 1
            if (qty > 0) {
                repository.updateQuantity(
                    item.id,
                    userId,
                    qty
                )
                repository.syncCartToServer(
                    userId = userId,
                    productId = item.id,
                    qty = qty
                )

            } else {
                repository.removeLocal(
                    item.id,
                    userId
                )
                repository.syncCartToServer(
                    userId = userId,
                    productId = item.id,
                    qty = 0
                )
            }
        }
    }
    private fun updateState(items: List<CartItem>) {
        val total = items.sumOf { it.price * it.qty }
        _state.value = CartState(
            cartItem = items,
            totalBill = total,
            isLoading = false  // ✅ yeh add karo
        )
    }
}