package com.mysanjeevni.mysanjeevni.features.support.ticket.data.repository


import android.util.Log
import androidx.compose.ui.text.toLowerCase
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.support.ticket.data.dto.CreateTicketRequestDto
import com.mysanjeevni.mysanjeevni.features.support.ticket.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.model.CreateTicketResult
import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.model.Ticket
import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.repository.SupportRepository
import javax.inject.Inject
import kotlin.collections.emptyList

class SupportRepositoryImpl @Inject constructor(
    private val api: ApiService
) : SupportRepository {

    companion object {
        private const val TAG = "SUPPORT_REPO"
    }

    override suspend fun raiseTicket(
        userId: String,
        userName: String,
        email: String,
        role: String,
        category: String,
        subject: String,
        message: String
    ): Result<CreateTicketResult> {

        return try {

            Log.d(TAG, "========== CREATE TICKET ==========")
            Log.d(TAG, "userId=$userId")
            Log.d(TAG, "userName=$userName")
            Log.d(TAG, "email=$email")
            Log.d(TAG, "role=$role")
            Log.d(TAG, "category=$category")
            Log.d(TAG, "subject=$subject")
            Log.d(TAG, "message=$message")

            val response = api.raiseTicket(
                userId = userId,
                userName = userName,
                email = email,
                role = role.toLowerCase(),
                request = CreateTicketRequestDto(
                    category = category,
                    subject = subject,
                    message = message
                )
            )

            Log.d(TAG, "Response Code = ${response.code()}")

            if (response.isSuccessful) {

                val body = response.body()

                Log.d(TAG, "Success Body = $body")

                Result.success(
                    body!!.toDomain()
                )

            } else {

                val error = response.errorBody()?.string()

                Log.e(TAG, "API ERROR = $error")

                Result.failure(
                    Exception(error ?: "Unknown Error")
                )
            }

        } catch (e: Exception) {

            Log.e(TAG, "EXCEPTION = ${e.message}", e)

            Result.failure(e)
        }
    }

    override suspend fun getTickets(
        userId: String
    ): Result<List<Ticket>> {

        return try {

            Log.d(TAG, "========== GET TICKETS ==========")
            Log.d(TAG, "userId=$userId")

            val response = api.getTickets(userId)

            Log.d(TAG, "Response Code = ${response.code()}")

            if (response.isSuccessful) {

                val tickets =
                    response.body()
                        ?.tickets
                        ?.map { it.toDomain() }
                        ?: emptyList()

                Log.d(TAG, "Tickets Count = ${tickets.size}")

                Result.success(tickets)

            } else {

                val error = response.errorBody()?.string()

                Log.e(TAG, "API ERROR = $error")

                Result.failure(
                    Exception(error ?: "Unknown Error")
                )
            }

        } catch (e: Exception) {

            Log.e(TAG, "EXCEPTION = ${e.message}", e)

            Result.failure(e)
        }
    }
}