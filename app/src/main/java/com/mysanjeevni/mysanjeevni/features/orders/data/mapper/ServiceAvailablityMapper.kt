package com.mysanjeevni.mysanjeevni.features.orders.data.mapper

import com.mysanjeevni.mysanjeevni.features.orders.data.dto.ServiceabilityResponseDto
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.Serviceability

fun ServiceabilityResponseDto.toDomain(): Serviceability {

    return Serviceability(

        serviceable = data.serviceable,

       courierName = data.recommended.courierName ?: "No service available",

        deliveryCharge = data.recommended.rate,

        estimatedDeliveryDate = data.recommended.estimatedDeliveryDate,

        estimatedDeliveryDays = data.recommended.estimatedDeliveryDays,

        codAvailable = data.codAvailable
    )
}