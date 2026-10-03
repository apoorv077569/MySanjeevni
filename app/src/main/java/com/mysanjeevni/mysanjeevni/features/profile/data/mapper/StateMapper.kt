package com.mysanjeevni.mysanjeevni.features.profile.data.mapper

import com.mysanjeevni.mysanjeevni.features.profile.data.dto.StateDto
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.State

fun StateDto.toDomain(): State {
    return State(
        name = name,
        stateCode = state_code
    )
}