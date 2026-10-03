package com.mysanjeevni.mysanjeevni.features.labs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.labs.data.datasource.LabPagingSource
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.CreateLabBookingRequestDto
import com.mysanjeevni.mysanjeevni.features.labs.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabBooking
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTest
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTestDetail
import com.mysanjeevni.mysanjeevni.features.labs.domain.repository.LabsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.AuthApiService
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.slot.SlotsRequestDto
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.Serviceability
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.SlotsRequestParams
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.SlotsResult
import com.mysanjeevni.mysanjeevni.utils.SessionManager

class LabsRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val authApi: AuthApiService,
    sessionManager: SessionManager
) : LabsRepository {

    val userId = sessionManager.getUserId()

    override fun getLabTestPaging(): Flow<PagingData<LabTest>> {

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            )
        ) {
            LabPagingSource(api)
        }.flow
    }

    override suspend fun getLabTestDetail(
        id: String
    ): Result<LabTestDetail> {

        return try {

            val response = api.getLabTestById(id)

            if (response.isSuccessful) {

                Result.success(
                    response.body()!!.test.toDomain()
                )

            } else {

                Result.failure(
                    Exception("Failed")
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    override suspend fun bookLabTest(
        request: CreateLabBookingRequestDto
    ): Result<LabBooking> {

        Log.d("LAB_BOOKING_REPO", "==============")
        Log.d("LAB_BOOKING_REPO", "API Call Started")
        Log.d("LAB_BOOKING_REPO", "Request = $request")

        return try {

            val response =
                api.createLabTestBooking(userId,request)
            Log.d("LAB_BOOKING_REPO","Url = ${response.raw().request.url}")

            Log.d(
                "LAB_BOOKING_REPO",
                "Response Code = ${response.code()}"
            )


            Log.d(
                "LAB_BOOKING_REPO",
                "Is Successful = ${response.isSuccessful}"
            )

            Log.d(
                "LAB_BOOKING_REPO",
                "Response Body = ${response.body()}"
            )

            if (!response.isSuccessful) {

                Log.e(
                    "LAB_BOOKING_REPO",
                    "Error Body = ${response.errorBody()?.string()}"
                )
            }

            if (response.isSuccessful) {

                Log.d(
                    "LAB_BOOKING_REPO",
                    "Booking Success"
                )

                Result.success(
                    response.body()!!.booking
                )

            } else {

                Log.e(
                    "LAB_BOOKING_REPO",
                    "Booking Failed"
                )
                Log.e(
                    "LAB_BOOKING_REPO",
                    "ErrorBody = ${response.errorBody()?.string()}"
                )

                Result.failure(
                    Exception(
                        "Booking failed. Code=${response.code()}"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                "LAB_BOOKING_REPO",
                "Exception = ${e.message}",
                e
            )

            Result.failure(e)
        }
    }

    override suspend fun checkServiceability(
        testId: String,
        pincode: String
    ): Result<Serviceability> {

        Log.d("LABS_REPO", "checkServiceability() called")
        Log.d("LABS_REPO", "testId=$testId, pincode=$pincode")

        return try {
            val response = api.checkServiceability(testId, pincode)

            Log.d("LABS_REPO", "Response code = ${response.code()}")

            if (response.isSuccessful && response.body() != null) {

                Log.d("LABS_REPO", "Response body = ${response.body()}")

                val domain = response.body()!!.toDomain()

                Log.d("LABS_REPO", "Mapped domain = $domain")

                Result.success(domain)
            } else {

                val errorMsg = response.errorBody()?.string() ?: "Serviceability check failed"

                Log.e("LABS_REPO", "Error response = $errorMsg")

                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {

            Log.e("LABS_REPO", "Exception = ${e.message}", e)

            Result.failure(e)
        }
    }

    override suspend fun searchSlots(
        params: SlotsRequestParams
    ): Result<SlotsResult> {

        Log.d("LABS_REPO", "searchSlots() called")
        Log.d("LABS_REPO", "params=$params")

        return try {
            val requestDto = SlotsRequestDto(
                testId = params.testId,
                testName = params.testName,
                appointmentDate = params.appointmentDate,
                pincode = params.pincode,
                patientName = params.patientName,
                patientAge = params.patientAge,
                patientGender = params.patientGender
            )

            val response = api.searchSlots(requestDto)

            Log.d("LABS_REPO", "Response code = ${response.code()}")

            if (response.isSuccessful && response.body() != null) {

                Log.d("LABS_REPO", "Response body = ${response.body()}")

                val domain = response.body()!!.toDomain()

                Log.d("LABS_REPO", "Mapped domain = $domain")

                Result.success(domain)
            } else {

                val errorMsg = response.errorBody()?.string() ?: "Slot search failed"

                Log.e("LABS_REPO", "Error response = $errorMsg")

                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {

            Log.e("LABS_REPO", "Exception = ${e.message}", e)

            Result.failure(e)
        }
    }
}