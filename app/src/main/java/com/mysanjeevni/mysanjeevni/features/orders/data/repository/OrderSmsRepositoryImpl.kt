package com.mysanjeevni.mysanjeevni.features.orders.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.SendOrderSmsRequest
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderSmsRepository
import com.mysanjeevni.mysanjeevni.features.prescription.data.remote.PrescriptionApiService
import javax.inject.Inject

class OrderSmsRepositoryImpl @Inject constructor(
    private val apiService: PrescriptionApiService
) : OrderSmsRepository {
    override suspend fun orderConfirmationSms(
        request: SendOrderSmsRequest
    ): Result<String> {

        return try {
            Log.d(TAG, "================================")
            Log.d(TAG, "SEND ORDER SMS STARTED")
            Log.d(TAG, "Phone      : ${request.phone}")
            Log.d(TAG, "Email   : ${request.email}")
            Log.d(TAG, "Order ID   : ${request.items}")
            Log.d(TAG, "Address : ${request.shippingAddress}")

            Log.d(TAG, "Request    : $request")
            Log.d(TAG, "Calling SMS API...")

            val response = apiService.sendOrderConfirmationSms(
                request = request
            )

            Log.d(TAG, "HTTP Code  : ${response.code()}")
            Log.d(TAG, "Successful : ${response.isSuccessful}")
            Log.d(TAG, "Body       : ${response.body()}")

            if (response.isSuccessful) {
                val body = response.body()

                if (body?.success == true) {
                    val message =
                        body.message ?: "Order confirmation SMS sent successfully"

                    Log.d(TAG, "SMS SUCCESS")
                    Log.d(TAG, "Message    : $message")
                    Log.d(TAG, "================================")

                    Result.success(message)
                } else {
                    val message =
                        body?.message ?: "SMS API returned unsuccessful response"

                    Log.e(TAG, "SMS API FAILURE")
                    Log.e(TAG, "Message    : $message")
                    Log.e(TAG, "================================")

                    Result.failure(Exception(message))
                }
            } else {
                val errorBody = response.errorBody()?.string()

                Log.e(TAG, "SMS HTTP FAILURE")
                Log.e(TAG, "HTTP Code  : ${response.code()}")
                Log.e(TAG, "Error Body : $errorBody")
                Log.e(TAG, "================================")

                Result.failure(
                    Exception(
                        errorBody ?: "Failed to send order confirmation SMS"
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "================================")
            Log.e(TAG, "SMS EXCEPTION OCCURRED")
            Log.e(TAG, "Type       : ${e.javaClass.simpleName}")
            Log.e(TAG, "Message    : ${e.message}")
            Log.e(TAG, "Exception  :", e)
            Log.e(TAG, "================================")

            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "ORDER_SMS_REPO"
    }
    
}