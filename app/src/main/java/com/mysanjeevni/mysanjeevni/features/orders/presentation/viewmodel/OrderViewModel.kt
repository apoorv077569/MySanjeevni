package com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.medicines.domain.useCase.GetMedicineByIdUseCase
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.Order
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.OrderUiModel
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderRepository
import com.mysanjeevni.mysanjeevni.features.orders.presentation.state.OrderState
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository,
    private val getMedicineByIdUseCase: GetMedicineByIdUseCase

) : ViewModel() {

    private val _selectedMedicine = MutableStateFlow<Medicine?>(null)
    val selectedMedicine: StateFlow<Medicine?> = _selectedMedicine.asStateFlow()

    private val _selectedAddress = MutableStateFlow<AddressItem?>(null)
    val selectedAddress = _selectedAddress.asStateFlow()

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> = _orderState.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    // ── Recent Order ───────────────────────────────────────────────────────
    private val _recentOrder = MutableStateFlow<Order?>(null)
    val recentOrder: StateFlow<Order?> = _recentOrder.asStateFlow()

    fun setRecentOrder(order: Order) {
        _recentOrder.value = order
    }
    // ──────────────────────────────────────────────────────────────────────

    fun setCartItems(items: List<CartItem>){
        _cartItems.value = items
        Log.d("ORDER_VM","Cart Saved = ${items.size}")
    }

    fun setMedicine(medicine: Medicine){
        _selectedMedicine.value = medicine
    }

    fun setAddress(address: AddressItem){
        _selectedAddress.value = address
    }

    fun getOrders(userId: String?) {
        viewModelScope.launch {

            Log.d("ORDER_VM", "getOrders() called")
            Log.d("ORDER_VM", "UserId = $userId")

            _orderState.value = OrderState.Loading

            repository.getOrders(userId)
                .onSuccess { orders ->

                    Log.d("ORDER_VM", "Orders fetched successfully")
                    Log.d("ORDER_VM", "Total Orders = ${orders.size}")
                    Log.d("ORDER_VM", "Orders Data = $orders")

//                    val orderUiModels = mutableListOf<OrderUiModel>()
//
//                    Log.d("ORDER_TIME","Orders API Success = ${System.currentTimeMillis()}")
//
//                    orders.forEach { order ->
//                        val productId =
//                            order.items.firstOrNull()?.productId
//                        var medicine: Medicine? = null
//
//                        if (!productId.isNullOrEmpty()) {
//                            Log.d(
//                                "ORDER_TIME",
//                                "Medicine Fetch Start ${order.id} = ${System.currentTimeMillis()}"
//                            )
//                            getMedicineByIdUseCase(productId)
//                                .onSuccess {
//                                    medicine = it
//                                    Log.d(
//                                        "ORDER_TIME",
//                                        "Medicine Fetch End ${order.id} = ${System.currentTimeMillis()}"
//                                    )
//                                }
//                        }
//
//                        orderUiModels.add(
//                            OrderUiModel(
//                                order = order,
//                                medicine = medicine
//                            )
//                        )
//                    }
//
//                    _orderState.value =
//                        OrderState.Success(orderUiModels)
//                    _recentOrder.value = orders.firstOrNull()

                    val orderUiModels = orders.map { order ->
                        OrderUiModel(
                            order = order,
                            medicine = null
                        )
                    }

                    _orderState.value = OrderState.Success(orderUiModels)
                    _recentOrder.value = orders.firstOrNull()
                }
                .onFailure { e ->

                    Log.e("ORDER_VM", "Failed to fetch orders")
                    Log.e("ORDER_VM", "Error = ${e.message}", e)

                    _orderState.value =
                        OrderState.Error(e.message ?: "Something went wrong")
                }
        }
    }
}