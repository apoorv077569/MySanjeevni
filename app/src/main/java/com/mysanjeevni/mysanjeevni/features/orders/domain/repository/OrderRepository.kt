package com.mysanjeevni.mysanjeevni.features.orders.domain.repository

import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderRequest
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderResponse
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.RazorpayOrderDetailDto
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.RazorpayOrderDto
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.Order

interface OrderRepository {
    suspend fun getOrders(userId: String?): Result<List<Order>>
    suspend fun createOrder(request: CreateOrderRequest): Result<CreateOrderResponse>

    suspend fun getRazorpayOrder(
        orderId: String
    ): Result<RazorpayOrderDetailDto>
}