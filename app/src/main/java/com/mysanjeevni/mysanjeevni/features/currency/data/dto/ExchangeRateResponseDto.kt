package com.mysanjeevni.mysanjeevni.features.currency.data.dto


data class ExchangeRateResponseDto(
    val base: String?,
    val date: String?,
    val time_last_updated: Long?,
    val rates: Map<String, Double>?
)