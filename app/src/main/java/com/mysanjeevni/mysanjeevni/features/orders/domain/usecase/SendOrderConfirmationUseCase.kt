package com.mysanjeevni.mysanjeevni.features.orders.domain.usecase

import android.util.Log
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.SendOrderSmsRequest
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderSmsRepository
import javax.inject.Inject

class SendOrderConfirmationUseCase @Inject constructor(
    private val repository: OrderSmsRepository
) {
    companion object{
        private const val TAG = "ORDER_SMS_USECASE"
    }

    suspend operator fun invoke(
        request: SendOrderSmsRequest
    ):Result<String>{
        Log.d(TAG, "================================")
        Log.d(TAG, "SMS USE CASE STARTED")
        Log.d(TAG, "Phone    : ${request.phone}")
        Log.d(TAG, "Order ID : ${request.items}")
        Log.d(TAG, "Order ID : ${request.shippingAddress}")

        if (request.phone.isBlank()){
            Log.e(TAG,"Phone number is empty")
            return Result.failure(
                IllegalArgumentException("Phone number cannot be empty")
            )
        }
        if (request.items.isEmpty()){
            Log.e(TAG, "Order ID is empty")
            return Result.failure(
                IllegalArgumentException("Order ID cannot be empty")
            )
        }

        val result = repository.orderConfirmationSms(
            request
        )
        result.onSuccess { message ->
            Log.d(TAG, "SMS USE CASE SUCCESS")
            Log.d(TAG, "Message: $message")
        }
            .onFailure {error ->
                Log.e(TAG, "SMS USE CASE FAILED")
                Log.e(TAG, "Error: ${error.message}", error)
            }
        Log.d(TAG, "================================")
        return result
    }
}