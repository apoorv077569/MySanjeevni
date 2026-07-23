package com.mysanjeevni.mysanjeevni.features.home.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface LocationRepository {
    val city: StateFlow<String>
    fun updateCity(city: String)
    suspend fun fetchCurrentCity()
}