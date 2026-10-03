package com.mysanjeevni.mysanjeevni.features.consult.domnain.usecase


import com.mysanjeevni.mysanjeevni.features.consult.data.dto.DoctorConsultationSmsRequestDto
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.DoctorConsultationSmsResponseDto
import com.mysanjeevni.mysanjeevni.features.consult.domnain.repository.ConsultRepository
import javax.inject.Inject

class SendConsultationSmsUseCase @Inject constructor(
    private val repository: ConsultRepository
) {

    suspend operator fun invoke(
        request: DoctorConsultationSmsRequestDto
    ): Result<DoctorConsultationSmsResponseDto> {

        return repository.sendConsultationBookingSms(request)
    }
}