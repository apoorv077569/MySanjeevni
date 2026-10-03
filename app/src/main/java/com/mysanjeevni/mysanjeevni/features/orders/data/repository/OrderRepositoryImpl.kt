package com.mysanjeevni.mysanjeevni.features.orders.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.data.remote.api.AuthApiService
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CancelOrderRequest
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CancelOrderResponse
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderRequest
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderResponse
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.RazorpayOrderDetailDto
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.ServiceabilityRequestDto
import com.mysanjeevni.mysanjeevni.features.orders.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.Order
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.Serviceability
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val authApi: AuthApiService
) : OrderRepository {

    override suspend fun getOrders(
        userId: String?
    ): Result<List<Order>> {

        return try {

            Log.d("ORDER_API", "====================")
            Log.d("ORDER_API", "GET ORDERS START")
            Log.d("ORDER_API", "UserId = $userId")

            val response = api.getOrders(userId)

            Log.d(
                "ORDER_API",
                "URL = ${response.raw().request.url}"
            )

            Log.d(
                "ORDER_API",
                "Code = ${response.code()}"
            )

            Log.d(
                "ORDER_API",
                "IsSuccessful = ${response.isSuccessful}"
            )

            Log.d(
                "ORDER_API",
                "Raw Body = ${response.body()}"
            )

            if (response.isSuccessful) {

                val dtoOrders =
                    response.body()?.orders

                Log.d(
                    "ORDER_API",
                    "DTO Orders Count = ${dtoOrders?.size}"
                )

                Log.d(
                    "ORDER_API",
                    "DTO Orders = $dtoOrders"
                )

                val orders =
                    dtoOrders?.map { it.toDomain() }
                        ?: emptyList()

                Log.d(
                    "ORDER_API",
                    "Mapped Orders Count = ${orders.size}"
                )

                orders.forEachIndexed { index, order ->

                    Log.d(
                        "ORDER_API",
                        "Order[$index] = $order"
                    )

                    Log.d(
                        "ORDER_API",
                        "Order[$index] Items = ${order.items}"
                    )
                }

                Log.d(
                    "ORDER_TIME",
                    "API Response Received = ${System.currentTimeMillis()}"
                )

                Result.success(
                    orders
                )

            } else {

                val errorBody =
                    response.errorBody()?.string()

                Log.e(
                    "ORDER_API",
                    "ErrorBody = $errorBody"
                )

                Result.failure(
                    Exception(
                        "Error: ${response.code()}"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                "ORDER_API",
                "Exception = ${e.message}",
                e
            )

            Result.failure(e)
        }
    }

    override suspend fun createOrder(request: CreateOrderRequest): Result<CreateOrderResponse> {
        return try {
            val response = api.createOrder(request)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.d("ORDER_API", "Response Code = ${response.code()}")
                Log.d("ORDER_API", "Response Body = ${response.body()}")
                Log.d("ORDER_API", "Error Body = ${response.errorBody()?.string()}")

                return Result.failure(
                    Exception("Error ${response.code()} : $errorBody")
                )
            } else {
                val body = response.body()
                return if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Response body was null"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getRazorpayOrder(
        orderId: String
    ): Result<RazorpayOrderDetailDto> {

        return try {

            Log.d("ORDER_DETAIL_API", "====================")
            Log.d("ORDER_DETAIL_API", "Fetching Order")
            Log.d("ORDER_DETAIL_API", "OrderId = $orderId")

            val response = api.getRazorpayOrder(orderId)

            Log.d(
                "ORDER_DETAIL_API",
                "Response Code = ${response.code()}"
            )
            Log.d(
                "ORDER_DETAIL_API",
                "URL = ${response.raw().request.url}"
            )

            Log.d(
                "ORDER_DETAIL_API",
                "Response Message = ${response.message()}"
            )

            if (response.isSuccessful && response.body() != null) {

                val order = response.body()!!.order

                Log.d("ORDER_DETAIL_API", "SUCCESS")
                Log.d("ORDER_DETAIL_API", "Order Id = ${order.id}")
                Log.d("ORDER_DETAIL_API", "Status = ${order.status}")
                Log.d(
                    "ORDER_DETAIL_API",
                    "Payment Status = ${order.paymentStatus}"
                )
                Log.d(
                    "ORDER_DETAIL_API",
                    "Total Price = ${order.totalPrice}"
                )
                Log.d(
                    "ORDER_DETAIL_API",
                    "Delivery Address = ${order.deliveryAddress}"
                )

                Result.success(order)

            } else {

                val errorBody = response.errorBody()?.string()

                Log.e(
                    "ORDER_DETAIL_API",
                    "FAILED"
                )

                Log.e(
                    "ORDER_DETAIL_API",
                    "Error Body = $errorBody"
                )

                Result.failure(
                    Exception(
                        errorBody ?: "Failed to fetch order"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                "ORDER_DETAIL_API",
                "EXCEPTION = ${e.message}",
                e
            )

            Result.failure(e)
        }
    }

    override suspend fun checkServiceability(
        deliveryPincode: String
    ): Result<Serviceability> {

        return try {

            Log.d("SHIPROCKET_API", "==============================")
            Log.d("SHIPROCKET_API", "CHECK SERVICEABILITY START")
            Log.d("SHIPROCKET_API", "Delivery Pincode = $deliveryPincode")

            val response = api.checkServiceability(
                ServiceabilityRequestDto(deliveryPincode)
            )

            Log.d(
                "SHIPROCKET_API",
                "URL = ${response.raw().request.url}"
            )

            Log.d(
                "SHIPROCKET_API",
                "Response Code = ${response.code()}"
            )

            Log.d(
                "SHIPROCKET_API",
                "Is Successful = ${response.isSuccessful}"
            )

            Log.d(
                "SHIPROCKET_API",
                "Response Body = ${response.body()}"
            )

            if (response.isSuccessful && response.body() != null) {

                Log.d(
                    "SHIPROCKET_API",
                    "Success = ${response.body()!!.success}"
                )

                Log.d(
                    "SHIPROCKET_API",
                    "Serviceable = ${response.body()!!.data.serviceable}"
                )

                Log.d(
                    "SHIPROCKET_API",
                    "Courier = ${response.body()!!.data.recommended.courierName}"
                )

                Log.d(
                    "SHIPROCKET_API",
                    "Rate = ${response.body()!!.data.recommended.rate}"
                )

                Log.d(
                    "SHIPROCKET_API",
                    "ETA = ${response.body()!!.data.recommended.estimatedDeliveryDate}"
                )

                Log.d(
                    "SHIPROCKET_API",
                    "Days = ${response.body()!!.data.recommended.estimatedDeliveryDays}"
                )

                val result = response.body()!!.toDomain()

                Log.d(
                    "SHIPROCKET_API",
                    "Mapped Domain = $result"
                )

                Result.success(result)

            } else {

                val errorBody = response.errorBody()?.string()

                Log.e(
                    "SHIPROCKET_API",
                    "Error Body = $errorBody"
                )

                Result.failure(
                    Exception(
                        errorBody ?: "Unable to check delivery."
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                "SHIPROCKET_API",
                "Exception = ${e.message}",
                e
            )

            Result.failure(e)
        }
    }

    override suspend fun cancelOrder(
        orderId: String,
        userId: String
    ): Result<CancelOrderResponse> {

        return try {

            Log.d("CANCEL_ORDER", "==============================")
            Log.d("CANCEL_ORDER", "Calling Cancel Order API")
            Log.d("CANCEL_ORDER", "Order ID = $orderId")
            Log.d("CANCEL_ORDER", "User ID = $userId")

            val response = api.cancelOrder(
                CancelOrderRequest(
                    orderId = orderId,
                    status = "cancelled",
                    userId = userId
                )
            )

            Log.d(
                "CANCEL_ORDER",
                "Response Code = ${response.code()}"
            )

            Log.d(
                "CANCEL_ORDER",
                "Response Body = ${response.body()}"
            )

            if (response.isSuccessful) {

                val body = response.body()

                if (body != null) {

                    Log.d(
                        "CANCEL_ORDER",
                        "SUCCESS = ${body.message}"
                    )

                    Result.success(body)

                } else {

                    Result.failure(
                        Exception("Empty cancellation response")
                    )
                }

            } else {

                val errorBody =
                    response.errorBody()?.string()

                Log.e(
                    "CANCEL_ORDER",
                    "API FAILED = $errorBody"
                )

                Result.failure(
                    Exception(
                        errorBody ?: "Failed to cancel order"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                "CANCEL_ORDER",
                "Exception = ${e.message}",
                e
            )

            Result.failure(e)
        }
    }
}