package com.mysanjeevni.mysanjeevni.features.orders.domain.usecase


import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CancelOrderResponse
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderRepository
import javax.inject.Inject

class CancelOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {

    suspend operator fun invoke(
        orderId: String,
        userId: String
    ): Result<CancelOrderResponse> {

        return repository.cancelOrder(
            orderId = orderId,
            userId = userId
        )
    }
}