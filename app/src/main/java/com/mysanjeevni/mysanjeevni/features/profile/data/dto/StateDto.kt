package com.mysanjeevni.mysanjeevni.features.profile.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class StatesResponseDto(
    val error: Boolean,
    val msg: String,
    val data: StateDataDto?
)

@Serializable
data class StateDataDto(
    val name: String,
    val iso2: String? = null,
    val states: List<StateDto>
)

@Serializable
data class StateDto(
    val name: String,
    val state_code: String? = null
)

@Serializable
data class StateRequestDto(
    val country: String
)