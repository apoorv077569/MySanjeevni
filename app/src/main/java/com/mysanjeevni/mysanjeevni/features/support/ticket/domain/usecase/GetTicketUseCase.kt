package com.mysanjeevni.mysanjeevni.features.support.ticket.domain.usecase


import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.repository.SupportRepository
import javax.inject.Inject

class GetTicketsUseCase @Inject constructor(
    private val repository: SupportRepository
) {

    suspend operator fun invoke(
        userId: String
    ) = repository.getTickets(userId)
}