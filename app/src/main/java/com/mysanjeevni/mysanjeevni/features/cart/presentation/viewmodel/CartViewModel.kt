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

            Log.d("FLOW_TEST", "loadCart started")

            val userId = sessionManager.getUserId() ?: return@launch

            getCartUseCase(userId)
                .collect { entities ->

                    Log.d("FLOW_TEST", "Flow emitted -> ${entities.size}")

                    updateState(
                        entities.map {
                            CartItem(
                                id = it.productId,
                                name = it.name,
                                price = it.price,
                                originalPrice = it.originalPrice,
                                qty = it.quantity,
                                imageUrl = it.imageUrl,
                                stock = it.stock,
                                requirePrescription = it.requirePrescription
                            )
                        }
                    )
                }
        }
    }
//    fun loadCart() {
//        viewModelScope.launch {
//            val userId = sessionManager.getUserId()
//
//            Log.d("CART_DEBUG", "loadCart called - UserId: $userId")
//
//            if(userId == null) {
//                Log.e("CART_DEBUG", "UserId is null - Cart nahi load hoga!")
//                return@launch
//            }
//
//            getCartUseCase(userId).collect { entities ->
//                Log.d("CART_DEBUG", "Cart items from DB: ${entities.size}")
//                entities.forEach {
//                    Log.d("CART_DEBUG", "Item: ${it.productId} | Name: ${it.name} | Qty: ${it.quantity} | UserId: ${it.userId}")
//                }
//
//                val items = entities.map {
//                    CartItem(
//                        id = it.productId,
//                        name = it.name,
//                        price = it.price,
//                        originalPrice = it.originalPrice,
//                        qty = it.quantity,
//                        imageUrl = it.imageUrl,
//                        stock = it.stock,
//                        requirePrescription = it.requirePrescription
//                    )
//                }
//                updateState(items)
//            }
//        }
//    }

//    fun addToCart(item: CartItem) {
//        viewModelScope.launch {
//            val userId = sessionManager.getUserId()
//
//            Log.d("CART_DEBUG", "addToCart called - UserId: $userId")
//            Log.d("CART_DEBUG", "Item: ${item.id} | Name: ${item.name}")
//
//            if(userId == null) {
//                Log.e("CART_DEBUG", "UserId null - Cart add nahi hoga!")
//                return@launch
//            }
//
//            val entity = CartEntity(
//                productId = item.id,
//                userId = userId,
//                name = item.name,
//                price = item.price,
//                originalPrice = item.originalPrice,
//                quantity = 1,
//                imageUrl = item.imageUrl,
//                stock = item.stock
//            )
//            repository.addToCart(entity)
//
//            // 🔍 CHECK 4: Local DB mein add hua
//            Log.d("CART_DEBUG", "Item added to local DB")
//
//            loadCart()
//
//            try {
//                repository.syncCartToServer(
//                    userId = userId,
//                    productId = item.id,
//                    qty = 1
//                )
//                // 🔍 CHECK 5: Server sync hua
//                Log.d("CART_DEBUG", "Server sync successful")
//            } catch (e: Exception) {
//                Log.e("CART_DEBUG", "Exception = ${e.javaClass.simpleName}")
//                Log.e("CART_DEBUG", "Message = ${e.message}")
//            }
//        }
//    }

    fun addToCart(item: CartItem) {
        viewModelScope.launch {

            val userId = sessionManager.getUserId()

            Log.d("CART_STOCK", "==============================")
            Log.d("CART_STOCK", "Add to cart called")
            Log.d("CART_STOCK", "Product = ${item.name}")
            Log.d("CART_STOCK", "Product ID = ${item.id}")
            Log.d("CART_STOCK", "Available Stock = ${item.stock}")

            if (userId == null) {
                Log.e("CART_STOCK", "UserId is null")
                return@launch
            }

            // Completely out of stock
            if (item.stock <= 0) {
                Log.e("CART_STOCK", "Product is out of stock")

                _state.value = _state.value.copy(
                    error = "Out of stock"
                )

                return@launch
            }

            // Check existing cart quantity
            val existingItem = _state.value.cartItem
                .find { cartItem ->
                    cartItem.id == item.id
                }

            val currentQty = existingItem?.qty ?: 0

            Log.d("CART_STOCK", "Current Cart Qty = $currentQty")
            Log.d("CART_STOCK", "Available Stock = ${item.stock}")

            if (currentQty >= item.stock) {

                Log.e(
                    "CART_STOCK",
                    "Stock limit reached"
                )

                _state.value = _state.value.copy(
                    error = "Item stock limit reached"
                )

                return@launch
            }

            val newQty = currentQty + 1

            val entity = CartEntity(
                productId = item.id,
                userId = userId,
                name = item.name,
                price = item.price,
                originalPrice = item.originalPrice,
                quantity = newQty,
                imageUrl = item.imageUrl,
                stock = item.stock,
                requirePrescription = item.requirePrescription
            )

            repository.addToCart(entity)

            Log.d(
                "CART_STOCK",
                "Local cart updated. New Qty = $newQty"
            )

            try {
                repository.syncCartToServer(
                    userId = userId,
                    productId = item.id,
                    qty = newQty
                )

                Log.d(
                    "CART_STOCK",
                    "Server sync successful"
                )

            } catch (e: Exception) {

                Log.e(
                    "CART_STOCK",
                    "Server sync failed: ${e.message}",
                    e
                )
            }
        }
    }

    fun getUserId(): String {
        return sessionManager.getUserId().orEmpty()
    }
    private fun updateState(items: List<CartItem>) {

        Log.d(
            "CART_DEBUG",
            "updateState called - Items count: ${items.size}"
        )

        items.forEach { item ->

            Log.d(
                "CART_DEBUG",
                "State Item: ${item.id} | ${item.name} | Qty: ${item.qty}"
            )

            // RX CHECK
            Log.d(
                "RX_CART_DEBUG",
                "Name=${item.name} | " +
                        "requiresPrescription=${item.requirePrescription}"
            )
        }

        val total = items.sumOf {
            it.price * it.qty
        }

        Log.d(
            "CART_DEBUG",
            "Total: $total"
        )

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

            Log.d("CART_STOCK", "==============================")
            Log.d("CART_STOCK", "Increment clicked")
            Log.d("CART_STOCK", "Product = ${item.name}")
            Log.d("CART_STOCK", "Current Qty = ${item.qty}")
            Log.d("CART_STOCK", "Available Stock = ${item.stock}")

            if (item.stock <= 0) {

                _state.value = _state.value.copy(
                    error = "Out of stock"
                )

                Log.e(
                    "CART_STOCK",
                    "Product out of stock"
                )

                return@launch
            }

            if (item.qty >= item.stock) {

                _state.value = _state.value.copy(
                    error = "Stock limit reached"
                )

                Log.e(
                    "CART_STOCK",
                    "Cannot increment. Stock limit reached"
                )

                return@launch
            }

            val newQty = item.qty + 1

            repository.updateQuantity(
                productId = item.id,
                userId = userId,
                qty = newQty
            )

            Log.d(
                "CART_STOCK",
                "Local quantity updated = $newQty"
            )

            try {
                repository.syncCartToServer(
                    userId = userId,
                    productId = item.id,
                    qty = newQty
                )

                Log.d(
                    "CART_STOCK",
                    "Server sync successful"
                )

            } catch (e: Exception) {

                Log.e(
                    "CART_STOCK",
                    "Increment sync failed: ${e.message}",
                    e
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
    fun clearError() {
        _state.value = _state.value.copy(
            error = ""
        )
    }
}