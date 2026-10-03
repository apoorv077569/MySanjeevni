package com.mysanjeevni.mysanjeevni.features.currency.presentation.state

import com.mysanjeevni.mysanjeevni.features.currency.domain.model.CurrencyInfo

data class CurrencyState(
    val isLoading: Boolean = false,
    val currencyInfo: CurrencyInfo? = null,
    val error: String? = null
)