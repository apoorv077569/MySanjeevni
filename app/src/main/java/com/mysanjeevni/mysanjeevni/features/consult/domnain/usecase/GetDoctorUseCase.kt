package com.mysanjeevni.mysanjeevni.features.consult.domnain.usecase

import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Doctor
import com.mysanjeevni.mysanjeevni.features.consult.domnain.repository.ConsultRepository
import javax.inject.Inject

class GetDoctorUseCase @Inject constructor(private val repository: ConsultRepository) {
    suspend operator fun invoke(
        department: String?=null,
        search: String?=null
    ) : Result<List<Doctor>>{
        return repository.getDoctors(
            department = department,
            search = search
        )
    }
}