package com.mysanjeevni.mysanjeevni.features.consult.domnain.usecase

import com.mysanjeevni.mysanjeevni.features.consult.data.dto.AgoraTokenResponseDto
import com.mysanjeevni.mysanjeevni.features.consult.domnain.repository.AgoraRepository
import javax.inject.Inject

class GenerateAgoraTokenUseCase @Inject constructor(
    private val agoraRepository: AgoraRepository
) {

    suspend operator fun invoke(
        channelName: String,
        participantType: String
    ): Result<AgoraTokenResponseDto> {

        return agoraRepository.generateAgoraToken(
            channelName = channelName,
            participantType = participantType
        )
    }
}