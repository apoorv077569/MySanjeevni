package com.mysanjeevni.mysanjeevni.features.support.ticket.data.dto

data class CreateTicketRequestDto(
    val category: String,
    val subject: String,
    val message: String
)

