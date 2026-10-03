package com.mysanjeevni.mysanjeevni.features.home.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.mysanjeevni.mysanjeevni.features.home.domain.repository.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
): LocationRepository{
    private val _city = MutableStateFlow("India")

    override val city: StateFlow<String> = _city.asStateFlow()

    private var isManualSelection = false
    override fun updateCity(city: String) {
        isManualSelection = true
        Log.d(
            "ADDRESS_DEBUG",
            "Repository Update = $city"
        )
        _city.value = city
    }

    @SuppressLint("MissingPermission")
    override suspend fun fetchCurrentCity() {
        suspendCancellableCoroutine<Unit> {continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location == null){
                        Log.d(
                            "ADDRESS_DEBUG",
                            "Location is null"
                        )
                        if (continuation.isActive){
                            continuation.resume(Unit)
                        }
                        return@addOnSuccessListener
                    }
                    try{
                        val geoCoder = Geocoder(context, Locale.getDefault())
                        val addresses = geoCoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )
                        val cityName = addresses?.firstOrNull()
                            ?.locality?:"India"
                        if (!isManualSelection){
                            Log.d(
                                "ADDRESS_DEBUG",
                                "GPS City = $cityName"
                            )
                            _city.value = cityName
                        }else{
                            Log.d(
                                "ADDRESS_DEBUG",
                                "GPS Ignored. Manual city already selected."
                            )
                        }
                    }catch (e: Exception){
                        Log.e(
                            "ADDRESS_DEBUG",
                            "Failed to fetch city: ${e.message}",
                            e
                        )
                    }
                    if (continuation.isActive){
                        continuation.resume(Unit)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(
                        "ADDRESS_DEBUG",
                        "Location fetch failed: ${exception.message}",
                        exception
                    )
                    if (continuation.isActive){
                        continuation.resume(Unit)
                    }
                }
        }
    }
}