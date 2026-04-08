package com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.domain.usecase.GetCartUseCase
import com.mysanjeevni.mysanjeevni.features.cart.domain.usecase.UpdateCartUseCase
import com.mysanjeevni.mysanjeevni.features.cart.presentation.state.CartState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase
): ViewModel() {
    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state

    init {
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            getCartUseCase().collect { items ->
                updateStateWithNewItems(items)
            }
        }
    }
    fun incrementQty(item: CartItem){
        viewModelScope.launch {
            val updatedItems = updateCartUseCase(item.id,item.qty+1)
            updateStateWithNewItems(updatedItems)
        }
    }
    fun decrementQty(item: CartItem){
        viewModelScope.launch {
            val updatedItems = updateCartUseCase(item.id,item.qty-1)
            updateStateWithNewItems(updatedItems)
        }
    }
    private fun updateStateWithNewItems(items: List<CartItem>){
        val total = items.sumOf { it.price*it.qty }
        val delivery = if (total>500)0.0 else 40.0
        val finalBill = total + delivery

        val roundedBill = String.format("%.2f",finalBill).toDouble()

        _state.value = CartState(
            isLoading = false,
            cartItem = items,
            totalBill = roundedBill
        )
    }
}