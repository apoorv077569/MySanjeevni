package com.mysanjeevni.mysanjeevni.features.support.ticket.data.mapper

import com.mysanjeevni.mysanjeevni.features.support.ticket.data.dto.CreateTicketResponseDto
import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.model.CreateTicketResult


fun CreateTicketResponseDto.toDomain(): CreateTicketResult {
    return CreateTicketResult(
        success = success,
        message = message
    )
}