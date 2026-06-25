package com.mysanjeevni.mysanjeevni.features.support.ticket.domain.usecase


import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.repository.SupportRepository
import javax.inject.Inject

class RaiseTicketUseCase @Inject constructor(
    private val repository: SupportRepository
) {

    suspend operator fun invoke(
        userId: String,
        userName: String,
        email: String,
        role: String,
        category: String,
        subject: String,
        message: String
    ) = repository.raiseTicket(
        userId,
        userName,
        email,
        role,
        category,
        subject,
        message
    )
}