package com.mysanjeevni.mysanjeevni.features.support.returns.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.support.returns.data.dto.ReturnRequestDto
import com.mysanjeevni.mysanjeevni.features.support.returns.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.support.returns.domain.model.ReturnRequest
import com.mysanjeevni.mysanjeevni.features.support.returns.domain.repository.ReturnRepository
import javax.inject.Inject

private const val TAG = "ReturnRepository"

class ReturnRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ReturnRepository {

    override suspend fun submitReturn(
        request: ReturnRequestDto
    ): Result<String> {

        return try {

            Log.d(TAG, "==============================")
            Log.d(TAG, "SUBMIT RETURN API")
            Log.d(TAG, "userId=${request.userId}")
            Log.d(TAG, "userName=${request.userName}")
            Log.d(TAG, "userEmail=${request.userEmail}")
            Log.d(TAG, "orderId=${request.orderId}")
            Log.d(TAG, "productName=${request.productName}")
            Log.d(TAG, "reason=${request.reason}")
            Log.d(TAG, "preferredResolution=${request.preferredResolution}")

            val response = api.submitReturnRequest(request)

            Log.d(TAG, "Code = ${response.code()}")
            Log.d(TAG, "Message = ${response.message()}")

            if (response.isSuccessful) {

                Log.d(TAG, "Success Body = ${response.body()}")

                Result.success(
                    response.body()?.message ?: "Success"
                )

            } else {

                val errorBody = response.errorBody()?.string()

                Log.e(TAG, "API FAILED")
                Log.e(TAG, "Code = ${response.code()}")
                Log.e(TAG, "Error = $errorBody")

                Result.failure(
                    Exception(errorBody ?: "Failed")
                )
            }

        } catch (e: Exception) {

            Log.e(TAG, "Exception = ${e.message}", e)

            Result.failure(e)
        }
    }

    override suspend fun getReturns(
        userId: String
    ): Result<List<ReturnRequest>> {

        return try {

            Log.d(TAG, "==============================")
            Log.d(TAG, "GET RETURNS API")
            Log.d(TAG, "userId=$userId")

            val response = api.getReturnRequests(userId)

            Log.d(TAG, "Code = ${response.code()}")
            Log.d(TAG, "Message = ${response.message()}")

            if (response.isSuccessful) {

                val requests = response.body()?.requests ?: emptyList()

                Log.d(TAG, "Total Returns = ${requests.size}")

                requests.forEachIndexed { index, item ->

                    Log.d(
                        TAG,
                        """
                    Return[$index]
                    id=${item._id}
                    orderId=${item.orderId}
                    productName=${item.productName}
                    reason=${item.reason}
                    resolution=${item.preferredResolution}
                    status=${item.status}
                    supportNote=${item.supportNote}
                    """.trimIndent()
                    )
                }

                Result.success(
                    requests.map { it.toDomain() }
                )

            } else {

                val errorBody = response.errorBody()?.string()

                Log.e(TAG, "API FAILED")
                Log.e(TAG, "Code = ${response.code()}")
                Log.e(TAG, "Error = $errorBody")

                Result.failure(
                    Exception(errorBody ?: "Failed")
                )
            }

        } catch (e: Exception) {

            Log.e(TAG, "Exception = ${e.message}", e)

            Result.failure(e)
        }
    }
}