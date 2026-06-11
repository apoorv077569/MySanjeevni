package com.mysanjeevni.mysanjeevni.features.orders.presntation.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderRepository
import com.mysanjeevni.mysanjeevni.features.orders.presntation.state.OrderState
import com.mysanjeevni.mysanjeevni.features.profile.data.model.Address
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    private val _selectedMedicine = MutableStateFlow<Medicine?>(null)
    val selectedMedicine: StateFlow<Medicine?> = _selectedMedicine.asStateFlow()

    private val _selectedAddress = MutableStateFlow<AddressItem?>(null)
    val selectedAddress = _selectedAddress.asStateFlow()


    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> = _orderState.asStateFlow()

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

                    _orderState.value = OrderState.Success(orders)
                }
                .onFailure { e ->

                    Log.e("ORDER_VM", "Failed to fetch orders")
                    Log.e("ORDER_VM", "Error = ${e.message}", e)

                    _orderState.value =
                        OrderState.Error(e.message ?: "Something went wrong")
                }
        }
    }}