package com.mysanjeevni.mysanjeevni.features.payment.domain.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.features.labs.data.remote.LabPaymentApi
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.CreateRazorpayOrderRequest
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.CreateRazorpayOrderResponse
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.PaymentApi
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.PaymentRepository
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.VerifyPaymentRequest
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.VerifyResponse
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api: LabPaymentApi
) : PaymentRepository {

    override suspend fun createRazorpayOrder(
        request: CreateRazorpayOrderRequest
    ): Result<CreateRazorpayOrderResponse> {
        Log.d("PAYMENT_REPO", "API START")
        Log.d("PAYMENT_REPO", "Request = $request")
        return try {
            val response = api.createRazorpayOrder(request)
            Log.d(
                "PAYMENT_REPO",
                "URL = ${response.raw().request.url}"
            )
            Log.d("PAYMENT_REPO", "Code = ${response.code()}")
            Log.d("PAYMENT_REPO", "Body = ${response.body()}")
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Log.e(
                    "PAYMENT_REPO",
                    "Error = ${response.errorBody()?.string()}"
                )

                Result.failure(Exception("Error: ${response.code()}"))

            }
        } catch (e: Exception) {
            Log.e(
                "PAYMENT_REPO",
                "Exception",
                e
            )
            Result.failure(e)
        }
    }

    override suspend fun verifyPayment(
        request: VerifyPaymentRequest
    ): Result<VerifyResponse> {

        Log.d("PAYMENT_REPO", "===================")
        Log.d("PAYMENT_REPO", "VERIFY API START")
        Log.d("PAYMENT_REPO", "Request = $request")

        return try {

            val response = api.verifyPayment(request)

            Log.d("PAYMENT_REPO", "URL = ${response.raw().request.url}")
            Log.d("PAYMENT_REPO", "Code = ${response.code()}")
            Log.d("PAYMENT_REPO", "IsSuccessful = ${response.isSuccessful}")
            Log.d("PAYMENT_REPO", "Body = ${response.body()}")

            if (response.isSuccessful) {

                Result.success(response.body()!!)

            } else {

                val errorBody = response.errorBody()?.string()

                Log.e("PAYMENT_REPO", "ErrorBody = $errorBody")

                Result.failure(
                    Exception(errorBody)
                )
            }

        } catch (e: Exception) {

            Log.e(
                "PAYMENT_REPO",
                "Exception = ${e.message}",
                e
            )

            Result.failure(e)
        }
    }
}