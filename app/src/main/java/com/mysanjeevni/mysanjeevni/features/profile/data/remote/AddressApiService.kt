package com.mysanjeevni.mysanjeevni.features.profile.data.remote

import com.mysanjeevni.mysanjeevni.features.profile.data.dto.CountriesResponseDto
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.StateRequestDto
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.StatesResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AddressApiService {
    @GET("api/v0.1/countries")
    suspend fun getCountries(): CountriesResponseDto

    @POST("api/v0.1/countries/states")
    suspend fun getStates(
        @Body request: StateRequestDto
    ): StatesResponseDto
}