package com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.medicines.domain.useCase.GetMedicineByIdUseCase
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.CheckoutType
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.Order
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.OrderUiModel
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderRepository
import com.mysanjeevni.mysanjeevni.features.orders.presentation.state.OrderState
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Address
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
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _selectedAddress = MutableStateFlow<Address?>(null)
    val selectedAddress = _selectedAddress.asStateFlow()
    private val _deliveryCharge = MutableStateFlow(0.0)
    val deliveryCharge = _deliveryCharge.asStateFlow()

    private val _courierName = MutableStateFlow("")
    val courierName = _courierName.asStateFlow()

    private val _estimatedDeliveryDays = MutableStateFlow("")
    val estimatedDeliveryDays = _estimatedDeliveryDays.asStateFlow()

    private val _estimatedDeliveryDate = MutableStateFlow("")
    val estimatedDeliveryDate = _estimatedDeliveryDate.asStateFlow()

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> = _orderState.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    private val _checkoutType = MutableStateFlow<CheckoutType?>(null)
    val checkOutType = _checkoutType.asStateFlow()

    // ── Recent Order ───────────────────────────────────────────────────────
    private val _recentOrder = MutableStateFlow<Order?>(null)
    val recentOrder: StateFlow<Order?> = _recentOrder.asStateFlow()


    fun refresh(userId: String?){
        viewModelScope.launch {
            _isRefreshing.value = true
            getOrders(userId)
            _isRefreshing.value = false
        }
    }

    fun setRecentOrder(order: Order) {
        _recentOrder.value = order
    }
    // ──────────────────────────────────────────────────────────────────────

    fun setCartItems(items: List<CartItem>) {
        _cartItems.value = items
        Log.d("ORDER_VM", "Cart Saved = ${items.size}")
    }

    fun setMedicine(medicine: Medicine) {
        _selectedMedicine.value = medicine
    }

    fun setAddress(address: Address) {
        _selectedAddress.value = address
    }

    fun startCartCheckout() {

        Log.d("CHECKOUT_FLOW", "==============================")
        Log.d("CHECKOUT_FLOW", "STARTING CART CHECKOUT")
        Log.d(
            "CHECKOUT_FLOW",
            "Old Medicine = ${_selectedMedicine.value?.name}"
        )
        Log.d(
            "CHECKOUT_FLOW",
            "Old Type = ${_checkoutType.value}"
        )

        // Remove stale Buy Now product
        _selectedMedicine.value = null

        // Explicitly mark this flow as CART
        _checkoutType.value = CheckoutType.CART

        Log.d(
            "CHECKOUT_FLOW",
            "New Medicine = ${_selectedMedicine.value?.name}"
        )
        Log.d(
            "CHECKOUT_FLOW",
            "New Type = ${_checkoutType.value}"
        )
        Log.d("CHECKOUT_FLOW", "==============================")
    }

    fun startBuyNowCheckout() {

        Log.d("CHECKOUT_FLOW", "==============================")
        Log.d("CHECKOUT_FLOW", "STARTING BUY NOW CHECKOUT")

        _checkoutType.value = CheckoutType.BUY_NOW

        Log.d(
            "CHECKOUT_FLOW",
            "New Type = ${_checkoutType.value}"
        )
        Log.d("CHECKOUT_FLOW", "==============================")
    }

    fun clearCheckoutType() {
        _checkoutType.value = null
    }

    fun setShippingDetails(
        charge: Double,
        courier: String,
        days: String,
        date: String
    ) {

        _deliveryCharge.value = charge

        _courierName.value = courier

        _estimatedDeliveryDays.value = days

        _estimatedDeliveryDate.value = date
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