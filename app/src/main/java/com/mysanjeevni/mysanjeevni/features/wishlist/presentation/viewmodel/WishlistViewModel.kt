package com.mysanjeevni.mysanjeevni.features.wishlist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.data.local.entity.CartEntity
import com.mysanjeevni.mysanjeevni.features.cart.domain.usecase.AddToCartUseCase
import com.mysanjeevni.mysanjeevni.features.wishlist.data.dto.AddWishlistRequest
import com.mysanjeevni.mysanjeevni.features.wishlist.domain.model.WishlistItem
import com.mysanjeevni.mysanjeevni.features.wishlist.domain.usecase.AddToWishlistUseCase
import com.mysanjeevni.mysanjeevni.features.wishlist.domain.usecase.GetWishlistUseCase
import com.mysanjeevni.mysanjeevni.features.wishlist.domain.usecase.RemoveWishlistUseCase
import com.mysanjeevni.mysanjeevni.features.wishlist.presentation.state.WishlistState
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val removeWishlistUseCase: RemoveWishlistUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _state = MutableStateFlow(WishlistState())
    val state = _state.asStateFlow()

    fun addToWishlist(productId: String, productName: String, price: Double, image: String) {
        Log.d("WISHLIST_VM", "Add to wishlist Called ProductId=$productId")

        viewModelScope.launch {
            val userId =
                sessionManager.getUserId()
                    ?: return@launch
            addToWishlistUseCase(
                AddWishlistRequest(
                    userId = userId,
                    productId = productId,
                    productName = productName,
                    price = price,
                    image = image
                )
            )

            loadWishlist()
        }
    }

    fun removeFromWishlist(productId: String) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId() ?: return@launch
            removeWishlistUseCase(userId, productId)
            loadWishlist()
        }
    }
    fun loadWishlist() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            val userId = sessionManager.getUserId()
            if (userId == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "User not found"
                    )
                }
                return@launch
            }
            getWishlistUseCase(userId).onSuccess { items ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        items = items
                    )
                }
                Log.d("WISHLIST_VM", "Wishlist Count = ${items.size}")
            }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                    Log.e("WISHLIST_VM", "Error = ${exception.message}")
                }
            }
        }

    fun moveToCart(item: WishlistItem) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId() ?: return@launch

            addToCartUseCase(
                CartEntity(
                    productId = item.productId,
                    name = item.productName,
                    price = item.price,
                    imageUrl = item.image,
                    quantity = 1,
                    userId = userId,
                    originalPrice = item.price
                )
            )

            removeWishlistUseCase(userId, item.productId)
            loadWishlist()
        }
    }


}