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
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase
): ViewModel() {
    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state

    init {
        loadCart()
    }
    private fun loadCart(){
        viewModelScope.launch {
            _state.value = CartState(isLoading = true)
            try{
                val items = getCartUseCase()
                updateStateWithNewItems(items)
            }catch (e: Exception){
                _state.value = CartState(error = e.message?:"Unknown Error")
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

//        val total = items.fold(BigDecimal.ZERO){acc,item->
//            acc+(item.price.toBigDecimal()*item.qty.toBigDecimal())
//        }
//        val delivery = if(total > BigDecimal("500")){
//            BigDecimal.ZERO
//        }else{
//            BigDecimal("₹40.00")
//        }
//        val finalBill = (total+delivery).setScale(2, RoundingMode.HALF_UP)

        _state.value = CartState(
            isLoading = false,
            cartItem = items,
            totalBill = roundedBill
        )
    }
}