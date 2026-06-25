package com.mysanjeevni.mysanjeevni.features.orders.presntation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.medicines.domain.useCase.GetMedicineByIdUseCase
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderRepository
import com.mysanjeevni.mysanjeevni.features.orders.presntation.state.OrderDetailState
import com.mysanjeevni.mysanjeevni.features.profile.data.repository.AddressRepository
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val addressRepository: AddressRepository,
    private val sessionManager: SessionManager,
    private val getMedicineByIdUseCase: GetMedicineByIdUseCase
) : ViewModel() {

    var state by mutableStateOf(OrderDetailState())
        private set

    fun loadOrder(orderId: String) {

        viewModelScope.launch {

            try {

                state = state.copy(isLoading = true)

                val orderResult =
                    orderRepository.getRazorpayOrder(orderId)

                orderResult.onSuccess { order ->

                    val token = sessionManager.getToken() ?: ""
                    val userId = sessionManager.getUserId() ?: ""

                    val addresses =
                        addressRepository.fetchAddresses(
                            token,
                            userId
                        )

                    val selectedAddress =
                        addresses.find {
                            it.id == order.deliveryAddress
                        }

                    state = state.copy(
                        isLoading = false,
                        order = order,
                        address = selectedAddress
                    )
                    val medicines = mutableListOf<Medicine>()

                    order.items.forEach { item ->

                        if (item.productId.isNotEmpty()) {

                            getMedicineByIdUseCase(item.productId)
                                .onSuccess { medicine ->

                                    medicines.add(medicine)
                                }
                        }
                    }

                    state = state.copy(
                        isLoading = false,
                        order = order,
                        medicines = medicines
                    )
                }

            } catch (e: Exception) {

                state = state.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}