package com.mysanjeevni.mysanjeevni.features.support.ticket.data.dto

import com.google.gson.annotations.SerializedName

data class TicketDto(
    @SerializedName("_id")
    val id: String,
    val userId: String,
    val userName: String,
    val userEmail: String,
    val userRole: String,

    val subject: String,
    val category: String,
    val message: String,

    val status: String,
    val adminNote: String?,

    val resolvedAt: String?,
    val createdAt: String,
    val updatedAt: String
)