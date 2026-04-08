package com.mysanjeevni.mysanjeevni.core.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class LocationHelper(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    companion object {
        private const val TAG = "LocationHelper"
    }

    /**
     * Get current city with proper error handling
     * Uses getCurrentLocation instead of lastLocation for better accuracy
     */
    @SuppressLint("MissingPermission")
    fun getCurrentCity(onResult: (String) -> Unit) {
        try {
            // Check if Geocoder is available
            if (!Geocoder.isPresent()) {
                Log.w(TAG, "Geocoder not available on this device")
                onResult("Location unavailable")
                return
            }

            // Use getCurrentLocation instead of lastLocation for fresh location
            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    // Get city from location coordinates
                    getCityFromLocation(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        onResult = onResult
                    )
                } else {
                    // Fallback to last known location
                    Log.w(TAG, "Current location is null, trying last location")
                    getLastKnownCity(onResult)
                }
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Failed to get current location", exception)
                // Fallback to last known location
                getLastKnownCity(onResult)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error in getCurrentCity", e)
            onResult("Location unavailable")
        }
    }

    /**
     * Fallback method using last known location
     */
    @SuppressLint("MissingPermission")
    private fun getLastKnownCity(onResult: (String) -> Unit) {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        getCityFromLocation(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            onResult = onResult
                        )
                    } else {
                        Log.w(TAG, "Last location is also null")
                        onResult("Enable location")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Failed to get last location", exception)
                    onResult("Location unavailable")
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getLastKnownCity", e)
            onResult("Location unavailable")
        }
    }

    /**
     * Convert coordinates to city name with Android version handling
     */
    private fun getCityFromLocation(
        latitude: Double,
        longitude: Double,
        onResult: (String) -> Unit
    ) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ (API 33+) - Use new async API
                geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                    handleAddresses(addresses, onResult)
                }
            } else {
                // Older Android versions - Run on background thread
                Thread {
                    try {
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                        // Post result back to main thread
                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            handleAddresses(addresses, onResult)
                        }
                    } catch (e: IOException) {
                        Log.e(TAG, "Geocoder IOException (network issue)", e)
                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            onResult("Location unavailable")
                        }
                    } catch (e: IllegalArgumentException) {
                        Log.e(TAG, "Invalid coordinates", e)
                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            onResult("Invalid location")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Geocoder error", e)
                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            onResult("Location error")
                        }
                    }
                }.start()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error in getCityFromLocation", e)
            onResult("Location unavailable")
        }
    }

    /**
     * Process address list and extract city/pincode
     */
    private fun handleAddresses(addresses: List<Address>?, onResult: (String) -> Unit) {
        if (addresses.isNullOrEmpty()) {
            Log.w(TAG, "No addresses found")
            onResult("Unknown location")
            return
        }

        try {
            val address = addresses[0]

            // Extract city (try multiple fields)
            val city = address.locality           // City
                ?: address.subAdminArea           // District
                ?: address.adminArea              // State
                ?: address.subLocality            // Neighborhood
                ?: "Unknown"

            // Extract pincode
            val pincode = address.postalCode ?: ""

            // Format result
            val locationText = if (pincode.isNotEmpty()) {
                "$city, $pincode"
            } else {
                city
            }

            Log.d(TAG, "Location found: $locationText")
            onResult(locationText)

        } catch (e: Exception) {
            Log.e(TAG, "Error processing address", e)
            onResult("Location found")
        }
    }

    /**
     * Coroutine-based version for use in ViewModels
     */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentCityAsync(): String = withContext(Dispatchers.IO) {
        try {
            if (!Geocoder.isPresent()) {
                return@withContext "Geocoder unavailable"
            }

            // Get location synchronously in IO dispatcher
            val location = fusedLocationClient.lastLocation.await()
                ?: return@withContext "Location unavailable"

            val geocoder = Geocoder(context, Locale.getDefault())

            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )

            if (addresses.isNullOrEmpty()) {
                return@withContext "Unknown location"
            }

            val address = addresses[0]
            val city = address.locality
                ?: address.subAdminArea
                ?: address.adminArea
                ?: "Unknown"
            val pincode = address.postalCode ?: ""

            if (pincode.isNotEmpty()) "$city, $pincode" else city

        } catch (e: IOException) {
            Log.e(TAG, "Network error in geocoding", e)
            "Location unavailable"
        } catch (e: Exception) {
            Log.e(TAG, "Error in getCurrentCityAsync", e)
            "Location error"
        }
    }
}

// Extension function for Task.await()
suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T? {
    return kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { result ->
            continuation.resume(result) {}
        }
        addOnFailureListener { exception ->
            Log.e("TaskAwait", "Task failed", exception)
            continuation.resume(null) {}
        }
    }
}