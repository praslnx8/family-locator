package app.family.api

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class LocationFetcher(private val locationProviderClient: FusedLocationProviderClient) {

    private val locationFlowData = MutableStateFlow(
        Location("")
    )

    @SuppressLint("MissingPermission")
    fun listenToLocation(): Flow<Location> {
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.MINUTES.toMillis(15)
            fastestInterval = TimeUnit.MINUTES.toMillis(5)
            smallestDisplacement = 100.0F
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                locationResult.lastLocation?.let { location ->
                    locationFlowData.value = location
                }
            }
        }

        locationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        return locationFlowData
    }
}