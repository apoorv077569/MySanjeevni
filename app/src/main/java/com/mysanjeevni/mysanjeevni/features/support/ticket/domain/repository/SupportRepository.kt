package com.mysanjeevni.mysanjeevni.features.support.ticket.domain.repository

import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.model.CreateTicketResult
import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.model.Ticket


interface SupportRepository {

    suspend fun raiseTicket(
        userId: String,
        userName: String,
        email: String,
        role: String,
        category: String,
        subject: String,
        message: String
    ): Result<CreateTicketResult>

    suspend fun getTickets(
        userId: String
    ): Result<List<Ticket>>
}