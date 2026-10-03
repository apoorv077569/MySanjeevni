package com.mysanjeevni.mysanjeevni.features.currency.domain.repository


import com.mysanjeevni.mysanjeevni.features.currency.domain.model.CurrencyInfo

interface CurrencyRepository {

    suspend fun getCurrencyInfo(): Result<CurrencyInfo>
}