package app.family.api.apis

import android.location.Geocoder
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocalityApi(private val geocoder: Geocoder) {

    fun getLocality(lat: Double, lon: Double): Flow<String?> = callbackFlow {
        geocoder.getFromLocation(lat, lon, 1) { addressList ->
            addressList.firstOrNull()?.let {
                trySend(it.locality)
            }
        }
        awaitClose()
    }
}