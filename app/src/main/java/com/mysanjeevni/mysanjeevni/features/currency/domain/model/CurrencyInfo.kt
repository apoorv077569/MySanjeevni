package com.mysanjeevni.mysanjeevni.features.currency.domain.model


data class CurrencyInfo(
    val currencyCode: String,
    val currencySymbol: String,
    val exchangeRate: Double
)