package com.mysanjeevni.mysanjeevni.features.support.ticket.presentation.state

import com.mysanjeevni.mysanjeevni.features.support.ticket.domain.model.Ticket

data class TicketUiState(
    val isLoading: Boolean = false,
    val tickets: List<Ticket> = emptyList(),
    val successMessage: String? = null,
    val errorMessage: String? = null
)