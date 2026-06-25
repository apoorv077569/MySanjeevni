package com.mysanjeevni.mysanjeevni.features.support.returns.domain.usecase

import com.mysanjeevni.mysanjeevni.features.support.returns.domain.repository.ReturnRepository
import javax.inject.Inject

class GetReturnsUseCase @Inject constructor(
    private val repository: ReturnRepository
) {
    suspend operator fun invoke(
        userId: String
    ) = repository.getReturns(userId)
}