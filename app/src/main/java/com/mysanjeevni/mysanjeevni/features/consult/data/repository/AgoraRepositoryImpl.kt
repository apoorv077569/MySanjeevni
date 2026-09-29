package com.mysanjeevni.mysanjeevni.features.consult.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.AgoraTokenRequestDto
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.AgoraTokenResponseDto
import com.mysanjeevni.mysanjeevni.features.consult.domnain.repository.AgoraRepository
import javax.inject.Inject

class AgoraRepositoryImpl @Inject constructor(
    private val agoraApi: ApiService
) : AgoraRepository {

    override suspend fun generateAgoraToken(
        channelName: String,
        participantType: String
    ): Result<AgoraTokenResponseDto> {

        Log.d(TAG, "========================================")
        Log.d(TAG, "generateAgoraToken()")
        Log.d(TAG, "channelName: $channelName")
        Log.d(TAG, "participantType: $participantType")

        return try {

            val request = AgoraTokenRequestDto(
                channelName = channelName,
                participantType = participantType
            )

            Log.d(TAG, "Request: $request")

            val response = agoraApi.generateAgoraToken(request)

            Log.d(TAG, "HTTP Code: ${response.code()}")
            Log.d(TAG, "Is Successful: ${response.isSuccessful}")

            if (response.isSuccessful) {

                val body = response.body()

                if (body != null) {

                    Log.d(TAG, "Agora token generated successfully")
                    Log.d(TAG, "appId: ${body.appId}")
                    Log.d(TAG, "uid: ${body.uid}")
                    Log.d(TAG, "expiresIn: ${body.expiresIn}")

                    // Don't log the actual token.
                    Log.d(TAG, "token received: ${body.token.isNotEmpty()}")

                    Result.success(body)

                } else {

                    Log.e(TAG, "Response body is null")

                    Result.failure(
                        Exception("Empty Agora token response")
                    )
                }

            } else {

                val errorBody =
                    response.errorBody()?.string()

                Log.e(TAG, "Agora token API failed")
                Log.e(TAG, "HTTP Code: ${response.code()}")
                Log.e(TAG, "Error: $errorBody")

                Result.failure(
                    Exception(
                        "Agora token API failed: ${response.code()}"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Exception while generating Agora token",
                e
            )

            Result.failure(e)

        } finally {

            Log.d(TAG, "generateAgoraToken() finished")
            Log.d(TAG, "========================================")
        }
    }

    companion object {
        private const val TAG = "AgoraRepository"
    }
}