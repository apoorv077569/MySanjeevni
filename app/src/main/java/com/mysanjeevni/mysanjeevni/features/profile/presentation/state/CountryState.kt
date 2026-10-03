package com.mysanjeevni.mysanjeevni.features.profile.presentation.state

import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Country
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.State

data class LocationState(
    val countries: List<Country> = emptyList(),
    val states: List<State> = emptyList(),

    val isLoadingCountries: Boolean = false,
    val isLoadingStates: Boolean = false,

    val countryError: String? = null,
    val stateError: String? = null
)