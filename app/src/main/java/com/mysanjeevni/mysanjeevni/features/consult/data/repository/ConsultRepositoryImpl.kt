package com.mysanjeevni.mysanjeevni.features.consult.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.CancelConsultationRequestDto
import com.mysanjeevni.mysanjeevni.features.consult.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.consult.data.mapper.toDto
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.BookConsultationRequest
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.BookConsultationResponse
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.CancelConsultationResponse
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Consultation
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Doctor
import com.mysanjeevni.mysanjeevni.features.consult.domnain.repository.ConsultRepository
import javax.inject.Inject

class ConsultRepositoryImpl @Inject constructor(
    private val consultApi: ApiService
) : ConsultRepository {

    companion object {
        private const val TAG = "ConsultRepository"
    }

    override suspend fun getDoctors(
        department: String?,
        search: String?
    ): Result<List<Doctor>> {

        Log.d(
            TAG,
            "getDoctors() called | department=$department | search=$search"
        )

        return try {

            Log.d(TAG, "Calling GET /api/doctors")

            val response = consultApi.getDoctors(
                department = department,
                search = search
            )

            Log.d(
                TAG,
                "API Response | code=${response.code()} | message=${response.message()}"
            )

            if (response.isSuccessful) {

                val responseBody = response.body()

                Log.d(
                    TAG,
                    "Response body received | body=$responseBody"
                )

                val doctorsDto = responseBody?.doctors

                Log.d(
                    TAG,
                    "Doctors DTO count=${doctorsDto?.size ?: 0}"
                )

                val doctors = doctorsDto
                    ?.map { doctorDto ->

                        Log.d(
                            TAG,
                            "Mapping doctor | id=${doctorDto.id} | name=${doctorDto.name}"
                        )

                        doctorDto.toDomain()
                    }
                    ?: emptyList()

                Log.d(
                    TAG,
                    "Doctors mapped successfully | count=${doctors.size}"
                )

                doctors.forEach { doctor ->
                    Log.d(
                        TAG,
                        "Doctor | id=${doctor.id}, " +
                                "name=${doctor.name}, " +
                                "specialization=${doctor.specialization}, " +
                                "department=${doctor.department}, " +
                                "rating=${doctor.rating}, " +
                                "fee=${doctor.consultationFee}, " +
                                "available=${doctor.isAvailable}"
                    )
                }

                Result.success(doctors)

            } else {

                val errorBody = response.errorBody()?.string()

                Log.e(
                    TAG,
                    "API Error | code=${response.code()} | error=$errorBody"
                )

                Result.failure(
                    Exception(
                        errorBody ?: "Failed to fetch doctors"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Exception while fetching doctors",
                e
            )

            Result.failure(e)
        }
    }

    override suspend fun getConsultations(
        userId: String
    ): Result<List<Consultation>> {

        return try {

            Log.d(
                TAG,
                "Fetching consultations | userId=$userId"
            )

            val response = consultApi.getConsultations(userId)

            val consultations = response.consultations
                .orEmpty()
                .map { it.toDomain() }

            Log.d(
                TAG,
                "Consultations fetched successfully | count=${consultations.size}"
            )

            consultations.forEach { consultation ->

                Log.d(
                    TAG,
                    "Consultation: " +
                            "id=${consultation.id}, " +
                            "doctor=${consultation.doctorName}, " +
                            "date=${consultation.appointmentDate}, " +
                            "time=${consultation.allottedTime}, " +
                            "status=${consultation.status}"
                )
            }

            Result.success(consultations)

        } catch (exception: Exception) {

            Log.e(
                TAG,
                "Failed to fetch consultations",
                exception
            )

            Result.failure(exception)
        }
    }

    override suspend fun bookConsultation(
        request: BookConsultationRequest
    ): Result<BookConsultationResponse> {

        return try {

            Log.d(TAG, "POST /api/consultations")

            Log.d(
                TAG,
                """
                Book consultation request:
                userId=${request.userId}
                doctorId=${request.doctorId}
                patientName=${request.patientName}
                patientPhone=${request.patientPhone}
                patientEmail=${request.patientEmail}
                appointmentDate=${request.appointmentDate}
                consultationType=${request.consultationType}
                symptoms=${request.symptoms}
                razorpayOrderId=${request.razorpayOrderId}
                razorpayPaymentId=${request.razorpayPaymentId}
                razorpaySignature=${request.razorpaySignature}
                """.trimIndent()
            )

            val requestDto = request.toDto()

            Log.d(
                TAG,
                "Request DTO created successfully"
            )

            val response = consultApi.bookConsultation(requestDto)

            Log.d(
                TAG,
                "POST response code = ${response.code()}"
            )

            Log.d(
                TAG,
                "POST response message = ${response.message()}"
            )

            if (response.isSuccessful) {

                val body = response.body()

                Log.d(
                    TAG,
                    "POST response body = $body"
                )

                if (body == null) {

                    Log.e(
                        TAG,
                        "POST successful but response body is null"
                    )

                    return Result.failure(
                        Exception("Empty response from server")
                    )
                }

                val result = body.toDomain()

                Log.d(
                    TAG,
                    "Consultation booked successfully"
                )

                Log.d(
                    TAG,
                    "Server message = ${result.message}"
                )

                Log.d(
                    TAG,
                    "Consultation ID = ${result.consultation?.id}"
                )

                Result.success(result)

            } else {

                val errorBody = response.errorBody()?.string()

                Log.e(
                    TAG,
                    """
                    POST book consultation failed:
                    code=${response.code()}
                    message=${response.message()}
                    errorBody=$errorBody
                    """.trimIndent()
                )

                Result.failure(
                    Exception(
                        errorBody ?: "Failed to book consultation"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                TAG,
                "POST book consultation exception: ${e.message}",
                e
            )

            Result.failure(e)
        }
    }

    override suspend fun cancelConsultation(
        consultationId: String
    ): Result<CancelConsultationResponse> {

        return try {

            Log.d(
                TAG,
                "PUT /api/consultations/$consultationId"
            )

            val request = CancelConsultationRequestDto(
                status = "cancelled"
            )

            Log.d(
                TAG,
                "Cancel request: status=${request.status}"
            )

            val response = consultApi.cancelConsultation(
                consultationId = consultationId,
                request = request
            )

            Log.d(
                TAG,
                "Cancel response code = ${response.code()}"
            )

            Log.d(
                TAG,
                "Cancel response message = ${response.message()}"
            )

            if (response.isSuccessful) {

                val body = response.body()

                Log.d(
                    TAG,
                    "Cancel response body = $body"
                )

                if (body == null) {

                    Log.e(
                        TAG,
                        "Cancel successful but response body is null"
                    )

                    return Result.failure(
                        Exception("Empty response from server")
                    )
                }

                val result = body.toDomain()

                Log.d(
                    TAG,
                    "Consultation cancelled successfully"
                )

                Log.d(
                    TAG,
                    "Message = ${result.message}"
                )

                Log.d(
                    TAG,
                    "Refund ID = ${result.refundId}"
                )

                Log.d(
                    TAG,
                    "Refund status = ${result.refundStatus}"
                )

                Result.success(result)

            } else {

                val errorBody =
                    response.errorBody()?.string()

                Log.e(
                    TAG,
                    """
                Cancel consultation failed:
                code=${response.code()}
                message=${response.message()}
                errorBody=$errorBody
                """.trimIndent()
                )

                Result.failure(
                    Exception(
                        errorBody
                            ?: "Failed to cancel consultation"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Cancel consultation exception: ${e.message}",
                e
            )

            Result.failure(e)
        }
    }
}