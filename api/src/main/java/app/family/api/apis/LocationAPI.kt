package app.family.api.apis

import android.location.Location
import android.os.Looper
import android.util.Log
import app.family.api.models.LocationDto
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

class LocationAPI(
    private val locationProviderClient: FusedLocationProviderClient
) {

    fun fetchLocation(): Flow<LocationDto?> = flow {
        val location = fetchCurrentLocation().first()
        emit(location?.let { convertToLocationDto(location) })
    }

    fun listenToLocationChange(): Flow<LocationDto> = callbackFlow {
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.MINUTES.toMillis(10)
            fastestInterval = TimeUnit.MINUTES.toMillis(5)

        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                Log.i("Location API", "On Location Result callback")
                locationResult.lastLocation?.let { location ->
                    Log.i("Location API", "Location Fetched")
                    trySend(convertToLocationDto(location))
                }
            }
        }

        locationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            Log.w("Location API", "Location listen closed")
            locationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun fetchCurrentLocation(): Flow<Location?> = callbackFlow {
        locationProviderClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("Location API", "Last Location Fetched")
                trySend(it.result)
            } else {
                Log.e("Location API", "Error while fetching location " + it.exception?.message)
                trySend(null)
            }
        }
        awaitClose { }
    }

    private fun convertToLocationDto(location: Location): LocationDto {
        return LocationDto(location.latitude, location.longitude)
    }
}