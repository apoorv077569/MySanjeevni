package com.mysanjeevni.mysanjeevni.features.support.ticket.data.mapper

import com.mysanjeevni.mysanjeevni.features.support.ticket.data.dto.TicketDto
import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.model.Ticket


fun TicketDto.toDomain(): Ticket {
    return Ticket(
        id = id,
        category = category,
        subject = subject,
        message = message,
        status = status,
        createdAt = createdAt
    )
}