package com.mysanjeevni.mysanjeevni.features.orders.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.data.remote.api.AuthApiService
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderRequest
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderResponse
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.RazorpayOrderDetailDto
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.RazorpayOrderDto
import com.mysanjeevni.mysanjeevni.features.orders.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.Order
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

            val response = authApi.getOrders(userId)

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
            val response = authApi.createOrder(request)
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

            val response = authApi.getRazorpayOrder(orderId)

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
    }}