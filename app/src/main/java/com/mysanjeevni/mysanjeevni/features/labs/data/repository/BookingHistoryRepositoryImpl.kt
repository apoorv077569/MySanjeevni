package com.mysanjeevni.mysanjeevni.features.labs.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.data.remote.api.AuthApiService
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import com.mysanjeevni.mysanjeevni.features.labs.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.BookingHistory
import com.mysanjeevni.mysanjeevni.features.labs.domain.repository.BookingHistoryRepository
import javax.inject.Inject

class BookingHistoryRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authApi: AuthApiService,
    private val sessionManager: SessionManager
) : BookingHistoryRepository {

    override suspend fun getBookingHistory(): Result<List<BookingHistory>> {

        return try {

            val userId = sessionManager.getUserId()

            Log.d(
                "LAB_HISTORY",
                "UserId = $userId"
            )

            val response = authApi.getBookingHistory(userId)

            Log.d(
                "LAB_HISTORY",
                "URL = ${response.raw().request.url}"
            )

            Log.d(
                "LAB_HISTORY",
                "Code = ${response.code()}"
            )

            Log.d(
                "LAB_HISTORY",
                "IsSuccessful = ${response.isSuccessful}"
            )

            Log.d(
                "LAB_HISTORY",
                "Body = ${response.body()}"
            )

            if (!response.isSuccessful) {

                Log.e(
                    "LAB_HISTORY",
                    "ErrorBody = ${response.errorBody()?.string()}"
                )
            }

            if (response.isSuccessful) {

                val bookings = response.body()?.bookings
                    ?.map { it.toDomain() }
                    ?: emptyList()

                Log.d(
                    "LAB_HISTORY",
                    "Bookings Count = ${bookings.size}"
                )

                bookings.forEachIndexed { index, booking ->

                    Log.d(
                        "LAB_HISTORY",
                        "Booking[$index] = $booking"
                    )
                }

                Result.success(bookings)

            } else {

                Result.failure(
                    Exception("Failed: ${response.code()}")
                )
            }

        } catch (e: Exception) {

            Log.e(
                "LAB_HISTORY",
                "Exception = ${e.message}",
                e
            )

            Result.failure(e)
        }
    }

    override suspend fun cancelBooking(
        bookingId: String
    ): Result<String> {

        Log.d("LAB_CANCEL", "===================")
        Log.d("LAB_CANCEL", "Cancel Booking Called")
        Log.d("LAB_CANCEL", "BookingId = $bookingId")
        Log.d("LAB_CANCEL", "UserId = ${sessionManager.getUserId()}")

        return try {

            val response = authApi.cancelBooking(
                bookingId = bookingId,
                userId = sessionManager.getUserId()
            )

            Log.d("LAB_CANCEL", "URL = ${response.raw().request.url}")
            Log.d("LAB_CANCEL", "Code = ${response.code()}")
            Log.d("LAB_CANCEL", "IsSuccessful = ${response.isSuccessful}")
            Log.d("LAB_CANCEL", "Body = ${response.body()}")

            if (response.isSuccessful) {

                Log.d(
                    "LAB_CANCEL",
                    "Booking Cancel Success"
                )

                Result.success(
                    "Booking cancelled successfully"
                )

            } else {

                val errorBody = response.errorBody()?.string()

                Log.e(
                    "LAB_CANCEL",
                    "ErrorBody = $errorBody"
                )

                Result.failure(
                    Exception(
                        errorBody ?: "Failed to cancel booking"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                "LAB_CANCEL",
                "Exception",
                e
            )

            Result.failure(e)
        }
    }

    override suspend fun syncBooking(
        bookingId: String
    ): Result<BookingHistory> {

        return try {

            val response = authApi.syncBooking(
                bookingId = bookingId,
                userId = sessionManager.getUserId()
            )

            Log.d("LAB_SYNC", "Code = ${response.code()}")
            Log.d("LAB_SYNC", "Body = ${response.body()}")

            if (response.isSuccessful && response.body()?.booking != null) {

                Result.success(
                    response.body()!!.booking!!.toDomain()
                )

            } else {

                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Sync failed"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                "LAB_SYNC",
                "Exception",
                e
            )

            Result.failure(e)
        }
    }}