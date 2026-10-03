package com.mysanjeevni.mysanjeevni.features.consult.domnain.usecase

import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Consultation
import com.mysanjeevni.mysanjeevni.features.consult.domnain.repository.ConsultRepository
import javax.inject.Inject


class GetConsultationUseCase @Inject constructor(private val repository: ConsultRepository
) {
    suspend operator fun invoke(userId:String): Result<List<Consultation>>{
        return repository.getConsultations(userId =userId)
    }
}
