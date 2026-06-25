package com.mysanjeevni.mysanjeevni.features.support.chat.data.repository

import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.support.chat.data.dto.SendChatRequestDto
import com.mysanjeevni.mysanjeevni.features.support.chat.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.support.chat.domain.model.ChatMessage
import com.mysanjeevni.mysanjeevni.features.support.chat.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ChatRepository {

    override suspend fun sendMessage(
        userId: String,
        userName: String,
        message: String
    ): Result<List<ChatMessage>> {

        return try {

            val response = api.sendChatMessage(
                SendChatRequestDto(
                    userId = userId,
                    userName = userName,
                    message = message
                )
            )

            if (response.isSuccessful) {

                Result.success(
                    response.body()?.messages
                        ?.map { it.toDomain() }
                        ?: emptyList()
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

    override suspend fun getMessages(
        userId: String
    ): Result<List<ChatMessage>> {

        return try {

            val response = api.getChatMessages(userId)

            if (response.isSuccessful) {

                Result.success(
                    response.body()?.messages
                        ?.map { it.toDomain() }
                        ?: emptyList()
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
}