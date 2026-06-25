package com.mysanjeevni.mysanjeevni.features.labs.domain.usecase

import com.mysanjeevni.mysanjeevni.features.labs.domain.repository.LabsRepository
import javax.inject.Inject

class GetLabTestDetailUseCase @Inject constructor(
    private val repository: LabsRepository
) {

    suspend operator fun invoke(
        id: String
    ) = repository.getLabTestDetail(id)
}
