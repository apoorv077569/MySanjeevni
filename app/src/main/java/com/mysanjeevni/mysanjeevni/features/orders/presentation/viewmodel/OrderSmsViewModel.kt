package com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.SendOrderSmsRequest
import com.mysanjeevni.mysanjeevni.features.orders.domain.usecase.SendOrderConfirmationUseCase
import com.mysanjeevni.mysanjeevni.features.orders.presentation.state.OrderSmsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderSmsViewModel @Inject constructor(
    private val sendOrderConfirmationSmsUseCase:
    SendOrderConfirmationUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "ORDER_SMS_VM"
    }

    private val _smsState =
        MutableStateFlow<OrderSmsUiState>(
            OrderSmsUiState.Idle
        )

    val smsState: StateFlow<OrderSmsUiState> =
        _smsState.asStateFlow()

    fun sendOrderConfirmationSms(
        request: SendOrderSmsRequest
    ) {
        viewModelScope.launch {
            Log.d("ORDER_SMS_VM", "SMS REQUEST STARTED")
            Log.d("ORDER_SMS_VM", "Request: $request")

            _smsState.value = OrderSmsUiState.Loading

            sendOrderConfirmationSmsUseCase(request)
                .onSuccess { message ->
                    Log.d("ORDER_SMS_VM", "SUCCESS: $message")

                    _smsState.value =
                        OrderSmsUiState.Success(message)
                }
                .onFailure { error ->
                    Log.e("ORDER_SMS_VM", "FAILED", error)

                    _smsState.value =
                        OrderSmsUiState.Error(
                            error.message ?: "SMS failed"
                        )
                }
        }
    }

    fun resetSmsState() {
        Log.d(TAG, "Resetting SMS state")
        _smsState.value = OrderSmsUiState.Idle
    }
}