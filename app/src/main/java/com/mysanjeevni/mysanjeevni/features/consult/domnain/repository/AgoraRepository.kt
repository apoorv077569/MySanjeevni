package com.mysanjeevni.mysanjeevni.features.consult.domnain.repository

import com.mysanjeevni.mysanjeevni.features.consult.data.dto.AgoraTokenResponseDto

interface AgoraRepository {
    suspend fun generateAgoraToken(
        channelName:String,
        participantType: String
    ):Result<AgoraTokenResponseDto>
}