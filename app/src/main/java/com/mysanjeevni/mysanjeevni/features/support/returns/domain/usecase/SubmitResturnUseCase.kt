package com.mysanjeevni.mysanjeevni.features.support.returns.domain.usecase

import com.mysanjeevni.mysanjeevni.features.support.returns.data.dto.ReturnRequestDto
import com.mysanjeevni.mysanjeevni.features.support.returns.domain.repository.ReturnRepository
import javax.inject.Inject

class SubmitReturnUseCase @Inject constructor(
    private val repository: ReturnRepository
) {
    suspend operator fun invoke(
        request: ReturnRequestDto
    ) = repository.submitReturn(request)
}