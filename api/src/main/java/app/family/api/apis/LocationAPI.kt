package app.family.api.apis

import android.location.Location
import android.os.Looper
import app.family.api.models.LocationDto
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LocationAPI(
    private val locationProviderClient: FusedLocationProviderClient
) {

    fun fetchLocation(): Flow<LocationDto> {
        return fetchCurrentLocation().map { locationDto ->
            convertToLocationDto(locationDto)
        }
    }

    fun listenToLocationChange(): Flow<LocationDto> = callbackFlow {
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.MINUTES.toMillis(10)
            fastestInterval = TimeUnit.MINUTES.toMillis(5)

        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                locationResult.lastLocation?.let { location ->
                    Timber.i("Fetched location")
                    trySend(convertToLocationDto(location))
                } ?: Timber.w("Location not found")
            }
        }

        locationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose { locationProviderClient.removeLocationUpdates(locationCallback) }
    }

    private fun fetchCurrentLocation(): Flow<Location> = callbackFlow {
        locationProviderClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful) {
                Timber.i("Fetched Current Location")
                trySend(it.result)
                close()
            } else {
                Timber.e("Error fetching Location", it.exception)
                close(it.exception)
            }
        }
        awaitClose()
    }

    private fun convertToLocationDto(location: Location): LocationDto {
        return LocationDto(location.latitude, location.longitude)
    }
}