package com.mysanjeevni.mysanjeevni.features.orders.domain.repository

import com.mysanjeevni.mysanjeevni.features.orders.data.dto.SendOrderSmsRequest

interface OrderSmsRepository {
    suspend fun orderConfirmationSms(
        request: SendOrderSmsRequest
    ):Result<String>
}