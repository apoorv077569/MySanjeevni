package com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.cart.data.local.entity.CartEntity
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
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
            val userId = sessionManager.getUserId()

            // 🔍 CHECK 1: UserId mil rahi hai ki nahi
            Log.d("CART_DEBUG", "loadCart called - UserId: $userId")

            if(userId == null) {
                Log.e("CART_DEBUG", "UserId is null - Cart nahi load hoga!")
                return@launch
            }

            getCartUseCase(userId).collect { entities ->
                // 🔍 CHECK 2: DB se kitne items mile
                Log.d("CART_DEBUG", "Cart items from DB: ${entities.size}")
                entities.forEach {
                    Log.d("CART_DEBUG", "Item: ${it.productId} | Name: ${it.name} | Qty: ${it.quantity} | UserId: ${it.userId}")
                }

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
            val userId = sessionManager.getUserId()

            Log.d("CART_DEBUG", "addToCart called - UserId: $userId")
            Log.d("CART_DEBUG", "Item: ${item.id} | Name: ${item.name}")

            if(userId == null) {
                Log.e("CART_DEBUG", "UserId null - Cart add nahi hoga!")
                return@launch
            }

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

            // 🔍 CHECK 4: Local DB mein add hua
            Log.d("CART_DEBUG", "Item added to local DB")

            loadCart()

            try {
                repository.syncCartToServer(
                    userId = userId,
                    productId = item.id,
                    qty = 1
                )
                // 🔍 CHECK 5: Server sync hua
                Log.d("CART_DEBUG", "Server sync successful")
            } catch (e: Exception) {
                Log.e("CART_DEBUG", "Exception = ${e.javaClass.simpleName}")
                Log.e("CART_DEBUG", "Message = ${e.message}")
            }
        }
    }

    private fun updateState(items: List<CartItem>) {
        // 🔍 CHECK 6: State update ho rahi hai
        Log.d("CART_DEBUG", "updateState called - Items count: ${items.size}")
        items.forEach {
            Log.d("CART_DEBUG", "State Item: ${it.id} | ${it.name} | Qty: ${it.qty}")
        }

        val total = items.sumOf { it.price * it.qty }
        Log.d("CART_DEBUG", "Total: $total")

        _state.value = CartState(
            cartItem = items,
            totalBill = total,
            isLoading = false
        )
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
}