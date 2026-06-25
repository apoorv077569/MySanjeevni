package com.mysanjeevni.mysanjeevni.features.support.returns.data.mapper

import com.mysanjeevni.mysanjeevni.features.support.returns.data.dto.ReturnDto
import com.mysanjeevni.mysanjeevni.features.support.returns.domain.model.ReturnRequest

fun ReturnDto.toDomain() = ReturnRequest(
    id = _id ?: "",
    orderId = orderId,
    productName = productName,
    reason = reason,
    preferredResolution = preferredResolution,
    status = status,
    supportNote = supportNote ?: ""
)