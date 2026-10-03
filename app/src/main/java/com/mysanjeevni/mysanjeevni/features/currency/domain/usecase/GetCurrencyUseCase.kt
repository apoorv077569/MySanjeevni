package com.mysanjeevni.mysanjeevni.features.currency.domain.usecase


import com.mysanjeevni.mysanjeevni.features.currency.domain.model.CurrencyInfo
import com.mysanjeevni.mysanjeevni.features.currency.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetCurrencyUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(): Result<CurrencyInfo> {
        return repository.getCurrencyInfo()
    }
}