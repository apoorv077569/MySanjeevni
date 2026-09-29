package com.mysanjeevni.mysanjeevni.features.prescription.data.remote

import com.mysanjeevni.mysanjeevni.features.orders.data.dto.SendOrderSmsRequest
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.SendOrderSmsResponse
import com.mysanjeevni.mysanjeevni.features.prescription.data.dto.PrescriptionResponseDto
import com.mysanjeevni.mysanjeevni.features.prescription.domain.model.PrescriptionModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface PrescriptionApiService {
    @Multipart
    @POST("api/prescriptions/upload")
    suspend fun uploadPrescription(
        @Part file: MultipartBody.Part,
        @Part("productId") productId: RequestBody,
        @Part("productName") productName: RequestBody,
        @Part("userId") userId: RequestBody
    ): Response<PrescriptionModel>

    @POST("api/orders/with-sms")
    suspend fun sendOrderConfirmationSms(
        @Body request: SendOrderSmsRequest
    ): Response<SendOrderSmsResponse>

    @GET("api/prescriptions")
    suspend fun getPrescriptions(
        @Query("userId") userId: String,
        @Query("consultationId") consultationId: String? = null
    ): Response<PrescriptionResponseDto>

}