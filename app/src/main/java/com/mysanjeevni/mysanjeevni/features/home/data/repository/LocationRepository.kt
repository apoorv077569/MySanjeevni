package com.mysanjeevni.mysanjeevni.features.home.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class LocationRepository @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) {

    private val _city = MutableStateFlow("India")
    val city: StateFlow<String> = _city.asStateFlow()

    private var isManualSelection = false

    fun updateCity(city: String) {
        isManualSelection = true
        Log.d("ADDRESS_DEBUG", "Repository Update = $city")

        _city.value = city
    }

    @SuppressLint("MissingPermission")
    suspend fun fetchCurrentCity() {

        suspendCancellableCoroutine<Unit> { continuation ->

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->

                    if (location == null) {
                        continuation.resume(Unit)
                        return@addOnSuccessListener
                    }

                    try {

                        val geocoder = Geocoder(
                            context,
                            Locale.getDefault()
                        )

                        val addresses = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )

                        val cityName =
                            addresses?.firstOrNull()?.locality
                                ?: "India"
                        if (!isManualSelection) {

                            Log.d("ADDRESS_DEBUG", "GPS City = $cityName")


                            _city.value = cityName
                        }else{
                            Log.d(
                                "ADDRESS_DEBUG",
                                "GPS Ignored. Manual city already selected."
                            )
                        }

                    } catch (_: Exception) {
                    }

                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resume(Unit)
                }
        }
    }
}