package com.mysanjeevni.mysanjeevni.features.orders.domain.usecase

import com.mysanjeevni.mysanjeevni.features.orders.domain.model.Serviceability
import com.mysanjeevni.mysanjeevni.features.orders.domain.repository.OrderRepository
import javax.inject.Inject

class CheckServiceabilityUseCase @Inject constructor(
    private val repository: OrderRepository
) {

    suspend operator fun invoke(
        deliveryPincode: String
    ): Result<Serviceability> {

        return repository.checkServiceability(
            deliveryPincode
        )
    }
}