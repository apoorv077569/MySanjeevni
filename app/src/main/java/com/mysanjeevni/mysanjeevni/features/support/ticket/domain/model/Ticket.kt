package com.mysanjeevni.mysanjeevni.features.support.ticket.domain.model


data class Ticket(
    val id: String,
    val category: String,
    val subject: String,
    val message: String,
    val status: String,
    val createdAt: String
)