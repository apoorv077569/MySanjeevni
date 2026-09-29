package com.mysanjeevni.mysanjeevni.features.consult.domnain.repository

import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.BookConsultationRequest
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.BookConsultationResponse
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.CancelConsultationResponse
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Consultation
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Doctor

interface ConsultRepository {
    suspend fun getDoctors(
        department:String?=null,
        search: String ?= null
    ): Result<List<Doctor>>

    suspend fun getConsultations(
        userId: String
    ): Result<List<Consultation>>

    suspend fun bookConsultation(
        request: BookConsultationRequest
    ): Result<BookConsultationResponse>

    suspend fun cancelConsultation(
        consultationId: String
    ): Result<CancelConsultationResponse>
}